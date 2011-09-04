-- phpMyAdmin SQL Dump
-- version 3.4.4deb1
-- http://www.phpmyadmin.net
--
-- Хост: localhost
-- Время создания: Сен 04 2011 г., 19:46
-- Версия сервера: 5.1.58
-- Версия PHP: 5.3.8-1

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
-- Структура таблицы `object`
--

CREATE TABLE IF NOT EXISTS `object` (
  `object_id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_object_id` int(11) DEFAULT NULL,
  `object_template_id` int(11) DEFAULT NULL,
  `object_created_at` datetime DEFAULT NULL,
  `object_updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`object_id`),
  KEY `object_template_id` (`object_template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=COMPACT AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Структура таблицы `object_props`
--

CREATE TABLE IF NOT EXISTS `object_props` (
  `object_props_id` int(11) NOT NULL AUTO_INCREMENT,
  `object_id` int(11) NOT NULL,
  `prop_id` int(11) NOT NULL,
  `intval` int(11) DEFAULT NULL,
  `floatval` float DEFAULT NULL,
  `strval` varchar(100) DEFAULT NULL,
  `textval` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`object_props_id`,`object_id`,`prop_id`),
  KEY `R_1` (`object_id`),
  KEY `R_16` (`prop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Структура таблицы `object_template`
--

CREATE TABLE IF NOT EXISTS `object_template` (
  `object_template_id` int(11) NOT NULL AUTO_INCREMENT,
  `Object_Template_Name` varchar(20) DEFAULT NULL,
  `parent_object_template_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`object_template_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC AUTO_INCREMENT=2 ;

--
-- Дамп данных таблицы `object_template`
--

INSERT INTO `object_template` (`object_template_id`, `Object_Template_Name`, `parent_object_template_id`) VALUES
(1, 'Пользователь', NULL);

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC AUTO_INCREMENT=5 ;

--
-- Дамп данных таблицы `object_template_props`
--

INSERT INTO `object_template_props` (`object_template_id`, `prop_id`, `object_template_props_id`) VALUES
(1, 1, 1),
(1, 2, 3);

-- --------------------------------------------------------

--
-- Структура таблицы `property`
--

CREATE TABLE IF NOT EXISTS `property` (
  `prop_id` int(11) NOT NULL,
  `prop_type_id` int(11) NOT NULL,
  `prop_defvalue` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`prop_id`),
  KEY `R_11` (`prop_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

--
-- Дамп данных таблицы `property`
--

INSERT INTO `property` (`prop_id`, `prop_type_id`, `prop_defvalue`) VALUES
(1, 1, 'Имя'),
(2, 1, 'Фамилия');

-- --------------------------------------------------------

--
-- Структура таблицы `prop_type`
--

CREATE TABLE IF NOT EXISTS `prop_type` (
  `prop_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `prop_type_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`prop_type_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC AUTO_INCREMENT=6 ;

--
-- Дамп данных таблицы `prop_type`
--

INSERT INTO `prop_type` (`prop_type_id`, `prop_type_name`) VALUES
(1, 'Строка'),
(2, 'Целое число'),
(3, 'Дробеое число'),
(4, 'Дата'),
(5, 'Примечание');

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `object`
--
ALTER TABLE `object`
  ADD CONSTRAINT `object_ibfk_1` FOREIGN KEY (`object_template_id`) REFERENCES `object_template` (`object_template_id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `object_props`
--
ALTER TABLE `object_props`
  ADD CONSTRAINT `object_props_ibfk_1` FOREIGN KEY (`object_id`) REFERENCES `object` (`object_id`),
  ADD CONSTRAINT `object_props_ibfk_2` FOREIGN KEY (`prop_id`) REFERENCES `property` (`prop_id`);

--
-- Ограничения внешнего ключа таблицы `object_template_props`
--
ALTER TABLE `object_template_props`
  ADD CONSTRAINT `object_template_props_ibfk_1` FOREIGN KEY (`object_template_id`) REFERENCES `object_template` (`object_template_id`),
  ADD CONSTRAINT `object_template_props_ibfk_2` FOREIGN KEY (`prop_id`) REFERENCES `property` (`prop_id`);

--
-- Ограничения внешнего ключа таблицы `property`
--
ALTER TABLE `property`
  ADD CONSTRAINT `property_ibfk_1` FOREIGN KEY (`prop_type_id`) REFERENCES `prop_type` (`prop_type_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
