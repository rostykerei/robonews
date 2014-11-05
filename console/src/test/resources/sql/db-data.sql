--
-- Dumping data for table `tag_type`
--

LOCK TABLES `tag_type` WRITE;
/*!40000 ALTER TABLE `tag_type` DISABLE KEYS */;
INSERT INTO `tag_type` (`id`, `type`) VALUES (2,'LOCATION'),(4,'MISC'),(3,'ORGANIZATION'),(1,'PERSON');
/*!40000 ALTER TABLE `tag_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `image_type`
--

LOCK TABLES `image_type` WRITE;
/*!40000 ALTER TABLE `image_type` DISABLE KEYS */;
INSERT INTO `image_type` (`id`, `type`) VALUES (3,'GIF'),(1,'JPEG'),(2,'PNG');
/*!40000 ALTER TABLE `image_type` ENABLE KEYS */;
UNLOCK TABLES;


INSERT INTO channel (id,canonicalName,title,url,scale,facebookId,twitterId,googlePlusId,description,alexaRank,version) VALUES (1,'cnn.com','CNN','http://www.cnn.com/',3,null,null,null,null,0,0);
INSERT INTO channel (id,canonicalName,title,url,scale,facebookId,twitterId,googlePlusId,description,alexaRank,version) VALUES (2,'foxnews.com','FOX News','http://www.foxnews.com/',3,null,null,null,null,0,0);
INSERT INTO channel (id,canonicalName,title,url,scale,facebookId,twitterId,googlePlusId,description,alexaRank,version) VALUES (3,'nbcnews.com','NBC News','http://www.nbcnews.com/',3,null,null,null,null,0,0);
