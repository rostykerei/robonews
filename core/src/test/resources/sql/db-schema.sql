-- MySQL dump 10.13  Distrib 5.5.44, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: news-test
-- ------------------------------------------------------
-- Server version	5.5.44-0ubuntu0.12.04.1

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
-- Table structure for table `area`
--

DROP TABLE IF EXISTS `area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `area` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `level` int(10) unsigned NOT NULL,
  `leftIndex` int(10) unsigned NOT NULL,
  `rightIndex` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `countryId` int(10) unsigned DEFAULT NULL,
  `stateId` int(10) unsigned DEFAULT NULL,
  `canonicalName` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `url` varchar(255) NOT NULL,
  `scale` int(10) unsigned NOT NULL,
  `facebookId` varchar(255) DEFAULT NULL,
  `twitterId` varchar(255) DEFAULT NULL,
  `googlePlusId` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `alexaRank` int(10) unsigned NOT NULL DEFAULT '0',
  `active` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `channel_idx_1` (`canonicalName`),
  KEY `channel_idx_2` (`scale`),
  KEY `channel_idx_3` (`active`),
  KEY `channel_idx_4` (`countryId`),
  KEY `channel_idx_5` (`stateId`),
  CONSTRAINT `channel_fk_1` FOREIGN KEY (`countryId`) REFERENCES `country` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `channel_fk_2` FOREIGN KEY (`stateId`) REFERENCES `state` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `channel_image`
--

DROP TABLE IF EXISTS `channel_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `channel_image` (
  `channelId` int(10) unsigned NOT NULL,
  `typeId` tinyint(3) unsigned NOT NULL,
  `data` blob NOT NULL,
  PRIMARY KEY (`channelId`) USING BTREE,
  KEY `channel_image_fk_2` (`typeId`),
  CONSTRAINT `channel_image_fk_1` FOREIGN KEY (`channelId`) REFERENCES `channel` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `channel_image_fk_2` FOREIGN KEY (`typeId`) REFERENCES `image_type` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `channel_picture`
--

DROP TABLE IF EXISTS `channel_picture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `channel_picture` (
  `channelId` int(10) unsigned NOT NULL,
  `picture` blob NOT NULL,
  `version` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`channelId`) USING BTREE,
  CONSTRAINT `channel_picture_fk_1` FOREIGN KEY (`channelId`) REFERENCES `channel` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `country`
--

DROP TABLE IF EXISTS `country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `country` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `isoCode2` char(2) NOT NULL,
  `isoCode3` char(3) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `country_idx_1` (`isoCode2`),
  UNIQUE KEY `country_idx_2` (`isoCode3`)
) ENGINE=InnoDB AUTO_INCREMENT=231 DEFAULT CHARSET=utf8;
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
  `areaId` int(10) unsigned NOT NULL,
  `topicId` int(10) unsigned NOT NULL,
  `url` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `link` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `copyright` varchar(255) DEFAULT NULL,
  `imageUrl` varchar(255) DEFAULT NULL,
  `isVideo` bit(1) NOT NULL DEFAULT b'0',
  `lastCheck` DATETIME NOT NULL DEFAULT '1970-01-01 00:00:00',
  `plannedCheck` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `inProcessSince` DATETIME NULL DEFAULT NULL,
  `velocity` double NOT NULL DEFAULT '0',
  `minVelocityThreshold` double NOT NULL DEFAULT '0',
  `maxVelocityThreshold` double NOT NULL DEFAULT '60',
  `httpLastEtag` varchar(255) DEFAULT NULL,
  `httpLastModified` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `feed_idx_1` (`url`),
  KEY `feed_idx_2` (`channelId`),
  KEY `feed_idx_3` (`topicId`),
  KEY `feed_idx_4` (`inProcessSince`,`plannedCheck`),
  KEY `feed_fk_2` (`areaId`),
  CONSTRAINT `feed_fk_1` FOREIGN KEY (`channelId`) REFERENCES `channel` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `feed_fk_2` FOREIGN KEY (`areaId`) REFERENCES `area` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `feed_fk_3` FOREIGN KEY (`topicId`) REFERENCES `topic` (`id`) ON UPDATE CASCADE
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
  `typeId` tinyint(3) unsigned NOT NULL,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `size` bigint(20) NOT NULL,
  `width` smallint(6) NOT NULL,
  `height` smallint(6) NOT NULL,
  `ratio` float NOT NULL,
  `crcHash` bigint(20) NOT NULL,
  `pHash` binary(8) NOT NULL,
  `createdDate` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
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
  `typeId` tinyint(3) unsigned NOT NULL,
  `directory` char(6) NOT NULL,
  `seed` tinyint(3) unsigned NOT NULL,
  `width` smallint(6) NOT NULL,
  `height` smallint(6) NOT NULL,
  `ratio` float NOT NULL,
  `size` bigint(20) NOT NULL,
  `createdDate` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleteAfterDate` DATETIME NULL DEFAULT NULL,
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
  `id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(8) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `type` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `section`
--

DROP TABLE IF EXISTS `section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `section` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `active` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `section_idx_1` (`name`),
  KEY `section_idx_2` (`active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `section_lead_feed`
--

DROP TABLE IF EXISTS `section_lead_feed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `section_lead_feed` (
  `sectionId` int(10) unsigned NOT NULL,
  `feedId` int(10) unsigned NOT NULL,
  PRIMARY KEY (`sectionId`,`feedId`),
  KEY `section_lead_feed_idx_1` (`sectionId`),
  KEY `section_lead_feed_idx_2` (`feedId`),
  CONSTRAINT `section_lead_feed_fk_1` FOREIGN KEY (`sectionId`) REFERENCES `section` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `section_lead_feed_fk_2` FOREIGN KEY (`feedId`) REFERENCES `feed` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `state`
--

DROP TABLE IF EXISTS `state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `state` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `countryId` int(10) unsigned NOT NULL,
  `isoCode` varchar(8) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `state_idx_1` (`countryId`,`isoCode`),
  CONSTRAINT `state_fk_1` FOREIGN KEY (`countryId`) REFERENCES `country` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;
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
  `areaId` int(10) unsigned NOT NULL,
  `topicId` int(10) unsigned NOT NULL,
  `originalFeedId` int(10) unsigned NOT NULL,
  `guidHash` binary(20) NOT NULL,
  `contentHash` binary(20) NOT NULL,
  `title` varchar(255) NOT NULL,
  `author` varchar(255) DEFAULT NULL,
  `link` varchar(255) NOT NULL,
  `guid` varchar(255) NOT NULL,
  `isVideo` bit(1) NOT NULL DEFAULT b'0',
  `publicationDate` DATETIME NOT NULL,
  `adjustedPublicationDate` DATETIME NOT NULL,
  `createdDate` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `description` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `story_uid` (`uid`),
  UNIQUE KEY `story_idx_1` (`channelId`,`guidHash`),
  UNIQUE KEY `story_idx_2` (`channelId`,`contentHash`),
  KEY `story_idx_3` (`channelId`,`adjustedPublicationDate`),
  KEY `story_idx_4` (`areaId`,`topicId`,`adjustedPublicationDate`),
  KEY `story_idx_5` (`originalFeedId`,`adjustedPublicationDate`),
  KEY `story_idx_6` (`adjustedPublicationDate`),
  CONSTRAINT `story_fk_1` FOREIGN KEY (`channelId`) REFERENCES `channel` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `story_fk_2` FOREIGN KEY (`areaId`) REFERENCES `area` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `story_fk_3` FOREIGN KEY (`topicId`) REFERENCES `topic` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `story_fk_4` FOREIGN KEY (`originalFeedId`) REFERENCES `feed` (`id`) ON UPDATE CASCADE
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
  CONSTRAINT `story_image_fk_2` FOREIGN KEY (`imageId`) REFERENCES `image` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
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

--
-- Table structure for table `topic`
--

DROP TABLE IF EXISTS `topic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `topic` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `isPriority` bit(1) NOT NULL DEFAULT b'0',
  `level` int(10) unsigned NOT NULL,
  `leftIndex` int(10) unsigned NOT NULL,
  `rightIndex` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-08-05 22:21:28
