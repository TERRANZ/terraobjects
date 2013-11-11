-- MySQL dump 10.13  Distrib 5.1.61, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: terraobjects
-- ------------------------------------------------------
-- Server version	5.1.61-2-log

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
-- Table structure for table `object`
--

DROP TABLE IF EXISTS `object`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `object` (
  `object_id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_object_id` int(11) DEFAULT NULL,
  `object_template_id` int(11) DEFAULT NULL,
  `object_created_at` timestamp NULL DEFAULT NULL,
  `object_updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`object_id`),
  KEY `object_template_id` (`object_template_id`),
  CONSTRAINT `object_ibfk_1` FOREIGN KEY (`object_template_id`) REFERENCES `object_template` (`object_template_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `object`
--

LOCK TABLES `object` WRITE;
/*!40000 ALTER TABLE `object` DISABLE KEYS */;
/*!40000 ALTER TABLE `object` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `object_prop_list`
--

DROP TABLE IF EXISTS `object_prop_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `object_prop_list` (
  `object_property_list_id` int(11) NOT NULL AUTO_INCREMENT,
  `list_id` int(11) NOT NULL,
  `object_id` int(11) NOT NULL,
  `position` int(11) DEFAULT NULL,
  PRIMARY KEY (`object_property_list_id`,`object_id`),
  KEY `object_id` (`object_id`),
  CONSTRAINT `object_prop_list_ibfk_1` FOREIGN KEY (`object_id`) REFERENCES `object` (`object_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `object_prop_list`
--

LOCK TABLES `object_prop_list` WRITE;
/*!40000 ALTER TABLE `object_prop_list` DISABLE KEYS */;
/*!40000 ALTER TABLE `object_prop_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `object_props`
--

DROP TABLE IF EXISTS `object_props`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `object_props` (
  `object_props_id` int(11) NOT NULL AUTO_INCREMENT,
  `object_id` int(11) NOT NULL,
  `prop_id` int(11) NOT NULL,
  `prop_type` int(11) NOT NULL,
  `intval` int(11) DEFAULT NULL,
  `floatval` float DEFAULT NULL,
  `strval` longtext,
  `textval` varchar(500) DEFAULT NULL,
  `dateval` datetime DEFAULT NULL,
  `listval` int(11) DEFAULT NULL,
  PRIMARY KEY (`object_props_id`,`object_id`,`prop_id`),
  KEY `R_1` (`object_id`),
  KEY `R_16` (`prop_id`),
  CONSTRAINT `object_props_ibfk_1` FOREIGN KEY (`object_id`) REFERENCES `object` (`object_id`),
  CONSTRAINT `object_props_ibfk_2` FOREIGN KEY (`prop_id`) REFERENCES `property` (`prop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `object_props`
--

LOCK TABLES `object_props` WRITE;
/*!40000 ALTER TABLE `object_props` DISABLE KEYS */;
/*!40000 ALTER TABLE `object_props` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `object_template`
--

DROP TABLE IF EXISTS `object_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `object_template` (
  `object_template_id` int(11) NOT NULL AUTO_INCREMENT,
  `Object_Template_Name` varchar(20) DEFAULT NULL,
  `parent_object_template_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`object_template_id`)
) ENGINE=InnoDB AUTO_INCREMENT=201 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `object_template`
--

LOCK TABLES `object_template` WRITE;
/*!40000 ALTER TABLE `object_template` DISABLE KEYS */;
INSERT INTO `object_template` VALUES (100,'Message',0),(200,'Thread',0);
/*!40000 ALTER TABLE `object_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `object_template_props`
--

DROP TABLE IF EXISTS `object_template_props`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `object_template_props` (
  `object_template_id` int(11) NOT NULL,
  `prop_id` int(11) NOT NULL,
  `object_template_props_id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`object_template_props_id`),
  KEY `R_18` (`object_template_id`),
  KEY `R_19` (`prop_id`),
  CONSTRAINT `object_template_props_ibfk_1` FOREIGN KEY (`object_template_id`) REFERENCES `object_template` (`object_template_id`),
  CONSTRAINT `object_template_props_ibfk_2` FOREIGN KEY (`prop_id`) REFERENCES `property` (`prop_id`)
) ENGINE=InnoDB AUTO_INCREMENT=203 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `object_template_props`
--

LOCK TABLES `object_template_props` WRITE;
/*!40000 ALTER TABLE `object_template_props` DISABLE KEYS */;
INSERT INTO `object_template_props` VALUES (100,100,100),(100,101,101),(100,102,102),(100,103,103),(100,104,104),(100,105,105),(100,106,106),(100,107,107),(100,108,108),(100,109,109),(100,110,110),(100,111,111),(100,112,112),(100,113,113),(100,114,114),(100,115,115),(100,116,116),(100,117,117),(100,118,118),(100,119,119),(100,120,120),(100,121,121),(200,201,201),(200,202,202);
/*!40000 ALTER TABLE `object_template_props` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prop_type`
--

DROP TABLE IF EXISTS `prop_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prop_type` (
  `prop_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `prop_type_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`prop_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prop_type`
--

LOCK TABLES `prop_type` WRITE;
/*!40000 ALTER TABLE `prop_type` DISABLE KEYS */;
INSERT INTO `prop_type` VALUES (1,'Строка'),(2,'Целое число'),(3,'Дробеое число'),(4,'Заметка'),(5,'Дата'),(6,'Список');
/*!40000 ALTER TABLE `prop_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `property`
--

DROP TABLE IF EXISTS `property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `property` (
  `prop_id` int(11) NOT NULL AUTO_INCREMENT,
  `prop_type_id` int(11) NOT NULL,
  `prop_defvalue` varchar(20) DEFAULT NULL,
  `prop_name` varchar(20) NOT NULL DEFAULT 'unnamed',
  PRIMARY KEY (`prop_id`),
  KEY `R_11` (`prop_type_id`),
  CONSTRAINT `property_ibfk_1` FOREIGN KEY (`prop_type_id`) REFERENCES `prop_type` (`prop_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=203 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `property`
--

LOCK TABLES `property` WRITE;
/*!40000 ALTER TABLE `property` DISABLE KEYS */;
INSERT INTO `property` VALUES (100,2,'','getId'),(101,2,'','getLasthit'),(102,2,'','getNum'),(103,2,'','getBanned'),(104,1,'','getDate'),(105,2,'','getSize'),(106,2,'','getTimestamp'),(107,2,'','getClosed'),(108,1,'','getThumbnail'),(109,2,'','getParent'),(110,1,'','getVideo'),(111,1,'','getSubject'),(112,1,'','getName'),(113,1,'','getImage'),(114,1,'','getComment'),(115,2,'','getOp'),(116,2,'','getWidth'),(117,1,'','getSticky'),(118,2,'','getTn_width'),(119,2,'','getHeight'),(120,2,'','getTn_height'),(121,1,'','getBannnned'),(201,2,'','getThreadId'),(202,2,'','getStartMessage');
/*!40000 ALTER TABLE `property` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-03-29 13:04:10
