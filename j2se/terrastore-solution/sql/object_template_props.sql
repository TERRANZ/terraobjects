-- phpMyAdmin SQL Dump
-- version 3.4.5deb1
-- http://www.phpmyadmin.net
--
-- Хост: localhost
-- Время создания: Окт 17 2011 г., 15:51
-- Версия сервера: 5.1.58
-- Версия PHP: 5.3.8-2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- База данных: `terraobjects`
--

-- --------------------------------------------------------

--
-- Структура таблицы `object_template_props`
--

CREATE TABLE IF NOT EXISTS `object_template_props` (
  `object_template_id` int(11) NOT NULL,
  `prop_id` int(11) NOT NULL,
  `object_template_props_id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`object_template_props_id`),
  KEY `R_18` (`object_template_id`),
  KEY `R_19` (`prop_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC AUTO_INCREMENT=65 ;

--
-- Дамп данных таблицы `object_template_props`
--

INSERT INTO `object_template_props` (`object_template_id`, `prop_id`, `object_template_props_id`) VALUES
(4, 4, 7),
(4, 9, 8),
(5, 4, 9),
(5, 6, 10),
(5, 7, 11),
(5, 8, 12),
(5, 10, 13),
(5, 11, 14),
(5, 13, 15),
(5, 14, 16),
(5, 15, 17),
(5, 16, 18),
(6, 4, 19),
(6, 17, 20),
(6, 16, 21),
(7, 4, 22),
(7, 18, 23),
(7, 12, 24),
(7, 20, 25),
(7, 13, 26),
(8, 4, 27),
(8, 18, 28),
(8, 13, 29),
(8, 19, 30),
(8, 12, 31),
(8, 20, 32),
(9, 4, 33),
(9, 5, 34),
(9, 21, 35),
(9, 22, 36),
(9, 23, 37),
(9, 16, 38),
(10, 4, 39),
(10, 13, 40),
(10, 30, 41),
(10, 24, 42),
(11, 4, 43),
(11, 18, 44),
(12, 4, 45),
(12, 25, 46),
(13, 4, 47),
(13, 17, 48),
(13, 26, 49),
(13, 27, 50),
(14, 4, 51),
(14, 28, 52),
(14, 32, 53),
(14, 18, 54),
(14, 12, 55),
(14, 29, 56),
(15, 4, 57),
(15, 31, 58),
(15, 27, 59),
(16, 4, 60),
(16, 28, 61),
(16, 18, 62),
(16, 33, 63),
(16, 32, 64);

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `object_template_props`
--
ALTER TABLE `object_template_props`
  ADD CONSTRAINT `object_template_props_ibfk_1` FOREIGN KEY (`object_template_id`) REFERENCES `object_template` (`object_template_id`),
  ADD CONSTRAINT `object_template_props_ibfk_2` FOREIGN KEY (`prop_id`) REFERENCES `property` (`prop_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
