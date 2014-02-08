-- MySQL dump 10.13  Distrib 5.5.34, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: news-test
-- ------------------------------------------------------
-- Server version	5.5.34-0ubuntu0.12.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `isPriority` bit(1) NOT NULL DEFAULT b'0',
  `level` int(10) unsigned NOT NULL,
  `leftIndex` int(10) unsigned NOT NULL,
  `rightIndex` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` (`id`, `name`, `isPriority`, `level`, `leftIndex`, `rightIndex`) VALUES (1,'test-category-1','\0',0,1,2);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `channel`
--

DROP TABLE IF EXISTS `channel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `channel` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `url` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `version` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `channel_idx_1` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `channel`
--

LOCK TABLES `channel` WRITE;
/*!40000 ALTER TABLE `channel` DISABLE KEYS */;
INSERT INTO `channel` (`id`, `name`, `url`, `description`, `version`) VALUES (16,'test-channel-1','test-url-1',NULL,0);
/*!40000 ALTER TABLE `channel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feed`
--

DROP TABLE IF EXISTS `feed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feed` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `channelId` int(10) unsigned NOT NULL,
  `categoryId` int(10) unsigned NOT NULL,
  `url` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `link` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `copyright` varchar(255) DEFAULT NULL,
  `imageUrl` varchar(255) DEFAULT NULL,
  `isVideo` bit(1) NOT NULL DEFAULT b'0',
  `lastCheck` timestamp NULL DEFAULT NULL,
  `plannedCheck` timestamp NULL DEFAULT NULL,
  `inProcessSince` timestamp NULL DEFAULT NULL,
  `velocity` double NOT NULL DEFAULT '0',
  `minVelocityThreshold` double NOT NULL DEFAULT '0',
  `maxVelocityThreshold` double NOT NULL DEFAULT '60',
  `httpLastEtag` varchar(255) DEFAULT NULL,
  `httpLastModified` timestamp NULL DEFAULT NULL,
  `version` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `feed_idx_1` (`url`),
  KEY `feed_idx_2` (`channelId`),
  KEY `feed_idx_3` (`categoryId`),
  KEY `feed_idx_4` (`inProcessSince`,`plannedCheck`),
  CONSTRAINT `feed_fk_1` FOREIGN KEY (`channelId`) REFERENCES `channel` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `feed_fk_2` FOREIGN KEY (`categoryId`) REFERENCES `category` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feed`
--

LOCK TABLES `feed` WRITE;
/*!40000 ALTER TABLE `feed` DISABLE KEYS */;
INSERT INTO `feed` (`id`, `channelId`, `categoryId`, `url`, `name`, `link`, `description`, `author`, `copyright`, `imageUrl`, `isVideo`, `lastCheck`, `plannedCheck`, `inProcessSince`, `velocity`, `minVelocityThreshold`, `maxVelocityThreshold`, `httpLastEtag`, `httpLastModified`, `version`) VALUES (1,16,1,'test-url-1','test-feed-1','test-link-1',NULL,NULL,NULL,NULL,'\0',NULL,NULL,NULL,0,0,0,NULL,NULL,0);
/*!40000 ALTER TABLE `feed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `uid` char(11) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
  `sourceChannelId` int(10) unsigned NOT NULL,
  `sourceStoryId` bigint(20) unsigned NOT NULL,
  `typeId` int(2) unsigned NOT NULL,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `size` bigint(16) NOT NULL,
  `width` int(4) NOT NULL,
  `height` int(4) NOT NULL,
  `ratio` float NOT NULL,
  `crcHash` bigint(20) NOT NULL,
  `pHash` binary(8) NOT NULL,
  `createdDate` timestamp NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `image_uid` (`uid`),
  UNIQUE KEY `image_idx1` (`url`),
  UNIQUE KEY `image_idx2` (`crcHash`),
  KEY `image_idx_3` (`sourceChannelId`),
  KEY `image_idx_4` (`sourceStoryId`),
  KEY `image_idx_5` (`width`,`height`),
  KEY `image_idx_6` (`pHash`),
  KEY `image_idx_7` (`createdDate`),
  KEY `image_fk_3` (`typeId`),
  CONSTRAINT `image_fk_1` FOREIGN KEY (`sourceChannelId`) REFERENCES `channel` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `image_fk_2` FOREIGN KEY (`sourceStoryId`) REFERENCES `story` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `image_fk_3` FOREIGN KEY (`typeId`) REFERENCES `image_type` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image`
--

LOCK TABLES `image` WRITE;
/*!40000 ALTER TABLE `image` DISABLE KEYS */;
INSERT INTO `image` (`id`, `uid`, `sourceChannelId`, `sourceStoryId`, `typeId`, `url`, `size`, `width`, `height`, `ratio`, `crcHash`, `pHash`) VALUES (1,'jNNjs-nvKNY',16,1,1,'http://some-url.com/test.jpg',1000,640,480,1.33333,123,'');
/*!40000 ALTER TABLE `image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image_type`
--

DROP TABLE IF EXISTS `image_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image_type` (
  `id` int(2) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(8) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `type` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image_type`
--

LOCK TABLES `image_type` WRITE;
/*!40000 ALTER TABLE `image_type` DISABLE KEYS */;
INSERT INTO `image_type` (`id`, `type`) VALUES (3,'GIF'),(1,'JPEG'),(2,'PNG');
/*!40000 ALTER TABLE `image_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `story`
--

DROP TABLE IF EXISTS `story`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `story` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `uid` char(11) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
  `channelId` int(10) unsigned NOT NULL,
  `categoryId` int(10) unsigned NOT NULL,
  `originalFeedId` int(10) unsigned NOT NULL,
  `guidHash` binary(20) NOT NULL,
  `contentHash` binary(20) NOT NULL,
  `title` varchar(255) NOT NULL,
  `author` varchar(255) DEFAULT NULL,
  `link` varchar(255) NOT NULL,
  `guid` varchar(255) NOT NULL,
  `isVideo` bit(1) NOT NULL DEFAULT b'0',
  `publicationDate` timestamp NULL DEFAULT NULL,
  `createdDate` timestamp NULL DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `story_uid` (`uid`),
  UNIQUE KEY `story_idx_1` (`channelId`,`guidHash`),
  UNIQUE KEY `story_idx_2` (`channelId`,`contentHash`),
  KEY `story_idx_3` (`channelId`,`publicationDate`),
  KEY `story_idx_4` (`categoryId`,`publicationDate`),
  KEY `story_idx_5` (`originalFeedId`,`publicationDate`),
  KEY `story_idx_6` (`publicationDate`),
  CONSTRAINT `story_fk_1` FOREIGN KEY (`channelId`) REFERENCES `channel` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `story_fk_2` FOREIGN KEY (`categoryId`) REFERENCES `category` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `story_fk_3` FOREIGN KEY (`originalFeedId`) REFERENCES `feed` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `story`
--

LOCK TABLES `story` WRITE;
/*!40000 ALTER TABLE `story` DISABLE KEYS */;
INSERT INTO `story` (`id`, `uid`, `channelId`, `categoryId`, `originalFeedId`, `guidHash`, `contentHash`, `title`, `author`, `link`, `guid`, `isVideo`, `publicationDate`, `createdDate`, `description`) VALUES (1,'6bDAFv_u8eI',16,1,1,'�q>lw^�{��1�\"��=L�','�j�N�7T���\rB��oA!','test-story-title-1',NULL,'test-story-link-1','test-story-guid-1','\0','2014-01-13 19:33:29','2014-01-13 19:33:29','test-story-descr-1');
/*!40000 ALTER TABLE `story` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `story_tag`
--

DROP TABLE IF EXISTS `story_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `story_tag` (
  `storyId` bigint(20) unsigned NOT NULL,
  `tagId` int(10) unsigned NOT NULL,
  PRIMARY KEY (`storyId`,`tagId`),
  KEY `story_tag_idx_1` (`storyId`),
  KEY `story_tag_idx_2` (`tagId`),
  CONSTRAINT `story_tag_fk_1` FOREIGN KEY (`storyId`) REFERENCES `story` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `story_tag_fk_2` FOREIGN KEY (`tagId`) REFERENCES `tag` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `story_tag`
--

LOCK TABLES `story_tag` WRITE;
/*!40000 ALTER TABLE `story_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `story_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `typeId` int(10) unsigned NOT NULL,
  `freebase_mid` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `isAmbiguous` bit(1) NOT NULL DEFAULT b'0',
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tag_idx_1` (`freebase_mid`),
  KEY `tag_idx_2` (`name`),
  KEY `tag_idx_3` (`typeId`,`name`),
  CONSTRAINT `tag_fk_1` FOREIGN KEY (`typeId`) REFERENCES `tag_type` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag_alternative`
--

DROP TABLE IF EXISTS `tag_alternative`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag_alternative` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `tagId` int(10) unsigned NOT NULL,
  `typeId` int(10) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `confidence` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tag_alternative_idx_1` (`name`,`typeId`),
  KEY `tag_alternative_idx_2` (`tagId`),
  CONSTRAINT `tag_alternative_fk_2` FOREIGN KEY (`tagId`) REFERENCES `tag` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag_alternative`
--

LOCK TABLES `tag_alternative` WRITE;
/*!40000 ALTER TABLE `tag_alternative` DISABLE KEYS */;
/*!40000 ALTER TABLE `tag_alternative` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag_type`
--

DROP TABLE IF EXISTS `tag_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag_type` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `type` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag_type`
--

LOCK TABLES `tag_type` WRITE;
/*!40000 ALTER TABLE `tag_type` DISABLE KEYS */;
INSERT INTO `tag_type` (`id`, `type`) VALUES (2,'LOCATION'),(4,'MISC'),(3,'ORGANIZATION'),(1,'PERSON');
/*!40000 ALTER TABLE `tag_type` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-01-13 20:33:56
