-- MySQL dump 10.13  Distrib 5.5.35, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: news-test
-- ------------------------------------------------------
-- Server version	5.5.35-0ubuntu0.12.04.2

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
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
  `createdDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `image_idx_1` (`url`),
  UNIQUE KEY `image_idx_2` (`sourceChannelId`,`size`,`crcHash`),
  KEY `image_idx_3` (`sourceChannelId`),
  KEY `image_idx_4` (`sourceStoryId`),
  KEY `image_idx_5` (`width`,`height`),
  KEY `image_idx_6` (`pHash`),
  KEY `image_idx_7` (`createdDate`),
  KEY `image_fk_3` (`typeId`),
  CONSTRAINT `image_fk_1` FOREIGN KEY (`sourceChannelId`) REFERENCES `channel` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `image_fk_2` FOREIGN KEY (`sourceStoryId`) REFERENCES `story` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `image_fk_3` FOREIGN KEY (`typeId`) REFERENCES `image_type` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `image_copy`
--

DROP TABLE IF EXISTS `image_copy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image_copy` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `uid` char(11) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
  `imageId` int(10) unsigned NOT NULL,
  `typeId` int(2) unsigned NOT NULL,
  `directory` char(6) NOT NULL,
  `seed` int(2) unsigned NOT NULL,
  `width` int(4) NOT NULL,
  `height` int(4) NOT NULL,
  `ratio` float NOT NULL,
  `size` bigint(16) NOT NULL,
  `createdDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleteAfterDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `image_copy_uid` (`uid`),
  KEY `image_copy_idx_1` (`imageId`),
  KEY `image_copy_idx_2` (`typeId`),
  KEY `image_copy_idx_3` (`directory`),
  KEY `image_copy_idx_4` (`width`,`height`),
  KEY `image_copy_idx_5` (`width`,`ratio`),
  KEY `image_copy_idx_6` (`createdDate`),
  KEY `image_copy_idx_7` (`deleteAfterDate`),
  CONSTRAINT `image_copy_fk_1` FOREIGN KEY (`imageId`) REFERENCES `image` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `image_copy_fk_2` FOREIGN KEY (`typeId`) REFERENCES `image_type` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `publicationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `createdDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `story_image`
--

DROP TABLE IF EXISTS `story_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `story_image` (
  `storyId` bigint(20) unsigned NOT NULL,
  `imageId` int(10) unsigned NOT NULL,
  PRIMARY KEY (`storyId`,`imageId`),
  KEY `story_image_idx_1` (`storyId`),
  KEY `story_image_idx_2` (`imageId`),
  CONSTRAINT `story_image_fk_1` FOREIGN KEY (`storyId`) REFERENCES `story` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `story_image_fk_2` FOREIGN KEY (`imageId`) REFERENCES `image` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-02-09 19:10:58
