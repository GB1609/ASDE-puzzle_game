CREATE DATABASE  IF NOT EXISTS `puzzle_game` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `puzzle_game`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: puzzle_game
-- ------------------------------------------------------
-- Server version	5.7.21-log

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
-- Table structure for table `credentials`
--

DROP TABLE IF EXISTS `credentials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `credentials` (
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `credentials`
--

LOCK TABLES `credentials` WRITE;
/*!40000 ALTER TABLE `credentials` DISABLE KEYS */;
INSERT INTO `credentials` VALUES ('gb1609','1609'),('Woflan','WWWW');
/*!40000 ALTER TABLE `credentials` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gamematch`
--

DROP TABLE IF EXISTS `gamematch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gamematch` (
  `id` bigint(20) NOT NULL,
  `date` datetime(6) NOT NULL,
  `time` varchar(255) NOT NULL,
  `winner` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `USER_FK` (`winner`),
  CONSTRAINT `USER_FK` FOREIGN KEY (`winner`) REFERENCES `user` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gamematch`
--

LOCK TABLES `gamematch` WRITE;
/*!40000 ALTER TABLE `gamematch` DISABLE KEYS */;
INSERT INTO `gamematch` VALUES (1,'2019-01-08 11:06:45.272000','11:06:39','gb1609'),(2,'2019-01-08 11:08:49.419000','11:08:45','gb1609'),(3,'2019-01-08 11:09:25.444000','00:00:06','gb1609'),(4,'2019-01-08 11:09:59.451000','11:09:55','gb1609'),(5,'2019-01-08 11:10:20.492000','11:10:16','Woflan'),(6,'2019-01-08 11:10:37.065000','11:10:16','Woflan'),(7,'2019-01-08 11:10:58.952000','11:10:54','gb1609'),(8,'2019-01-08 11:11:24.306000','11:11:19','Woflan'),(9,'2019-01-08 11:12:19.262000','11:11:19','gb1609'),(10,'2019-01-08 11:19:30.439000','11:19:22','gb1609'),(11,'2019-01-08 11:20:46.214000','11:20:17','gb1609'),(12,'2019-01-08 11:21:51.707000','00:00:08','Woflan'),(13,'2019-01-08 11:23:00.564000','00:00:46','Woflan'),(14,'2019-01-08 11:24:03.608000','11:23:14','Woflan'),(15,'2019-01-08 11:31:22.422000','11:31:10','gb1609'),(16,'2019-01-08 11:32:00.590000','11:31:34','Woflan'),(17,'2019-01-08 11:32:50.811000','11:32:38','Woflan'),(18,'2019-01-08 11:35:40.121000','00:02:23','gb1609');
/*!40000 ALTER TABLE `gamematch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (19);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `username` varchar(255) NOT NULL,
  `avatar` varchar(255) NOT NULL,
  `firstName` varchar(255) NOT NULL,
  `lastName` varchar(255) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('gb1609','avatar_2.png','GIovanni','Brunetti'),('Woflan','avatar_10.png','Elena','Mastria');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_gamematch`
--

DROP TABLE IF EXISTS `user_gamematch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_gamematch` (
  `users_username` varchar(255) NOT NULL,
  `matches_id` bigint(20) NOT NULL,
  PRIMARY KEY (`users_username`,`matches_id`),
  KEY `FKs0evfhk0axd9pp4pdv2931kkk` (`matches_id`),
  CONSTRAINT `FKi5svo8k897ctwp9v9mpn74ggn` FOREIGN KEY (`users_username`) REFERENCES `user` (`username`),
  CONSTRAINT `FKs0evfhk0axd9pp4pdv2931kkk` FOREIGN KEY (`matches_id`) REFERENCES `gamematch` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_gamematch`
--

LOCK TABLES `user_gamematch` WRITE;
/*!40000 ALTER TABLE `user_gamematch` DISABLE KEYS */;
INSERT INTO `user_gamematch` VALUES ('gb1609',1),('Woflan',1),('gb1609',2),('Woflan',2),('gb1609',3),('Woflan',3),('gb1609',4),('Woflan',4),('Woflan',5),('gb1609',6),('Woflan',6),('gb1609',7),('Woflan',7),('gb1609',8),('Woflan',8),('gb1609',9),('Woflan',9),('gb1609',10),('Woflan',10),('gb1609',11),('Woflan',11),('gb1609',12),('Woflan',12),('gb1609',13),('Woflan',13),('gb1609',14),('Woflan',14),('gb1609',15),('Woflan',15),('gb1609',16),('Woflan',16),('gb1609',17),('Woflan',17),('gb1609',18),('Woflan',18);
/*!40000 ALTER TABLE `user_gamematch` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-01-08 11:38:26
