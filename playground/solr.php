<?php

mysql_connect("localhost", "root", "root");
mysql_select_db("news");

mysql_query("SET NAMES 'utf8'");

$res = mysql_query("SELECT id, uid, title, author, link, isVideo, DATE_FORMAT(publicationDate,'%Y-%m-%dT%TZ') as publicationDate, DATE_FORMAT(createdDate,'%Y-%m-%dT%TZ') as createdDate, description as text, channelId, originalFeedId, cluster_id FROM story ORDER BY id;");

while($row = mysql_fetch_assoc($res)) {

    $tags = array();
    $tags_res = mysql_query("select name from story_tag left join tag on tag.id = story_tag.tagId where story_tag.storyId = " . $row['id']);

    while($tag_row = mysql_fetch_assoc($tags_res)) {
        $tags[] = $tag_row['name'];
    }

    $row['tags'] = $tags;


    $ch = curl_init("http://localhost:8983/solr/news/update/json?commit=true");
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1 );
    curl_setopt($ch, CURLOPT_POSTFIELDS,  json_encode(array($row)));
    curl_setopt($ch, CURLOPT_POST, 1);
    curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-type: application/json'));
    echo curl_exec($ch);
    curl_close($ch);


    $mlt_json = json_decode(file_get_contents("http://localhost:8983/solr/news/mlt?q=id:" . $row['id'] .  "&mlt.fl=title,text,tags&fl=id,cluster_id,score&rows=1&mlt.match.include=false&wt=json&indent=true"), 1);

    //print_r($mlt_json);

    $num_found = $mlt_json['response']['numFound'];
    $max_score = $mlt_json['response']['maxScore'];


    if (count($mlt_json['response']['docs']) > 0) {
        if (array_key_exists("cluster_id", $mlt_json['response']['docs'][0])) {
            $cluster_id = $mlt_json['response']['docs'][0]['cluster_id'];
        }
        else {
            $cluster_id = $mlt_json['response']['docs'][0]['id'];
        }
    }
    else {
        $cluster_id = $row['id'];
    }

    mysql_query("UPDATE story SET cluster_id = ". $cluster_id .", cluster_similarity = " . $max_score . ", cluster_docs = " . $num_found . " WHERE id = " . $row['id'] . ";");

    $ch = curl_init("http://localhost:8983/solr/news/update/json?commit=true");
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1 );
    curl_setopt($ch, CURLOPT_POSTFIELDS,  json_encode(array(

        array("id" => $row['id'],
              "cluster_id" => array("set" => $cluster_id) )

    )));
    curl_setopt($ch, CURLOPT_POST, 1);
    curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-type: application/json'));
    echo curl_exec($ch);
    curl_close($ch);


    // exit();
}
