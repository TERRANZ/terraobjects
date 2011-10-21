-- phpMyAdmin SQL Dump
-- version 3.4.6deb1
-- http://www.phpmyadmin.net
--
-- Хост: localhost
-- Время создания: Окт 21 2011 г., 15:11
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

DELIMITER $$
--
-- Процедуры
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `create_obj_prop`(templ_id INT,obj_id INT)
BEGIN
	DECLARE no_more_props INT DEFAULT 0;
	DECLARE prop INT DEFAULT 0;	
	DECLARE props_cur CURSOR FOR SELECT prop_id FROM object_template_props WHERE object_template_id = templ_id;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET no_more_props = 1;	
	
	OPEN props_cur;
	FETCH props_cur INTO prop;
	REPEAT
		INSERT INTO `terraobjects`.`object_props` (`object_id`,`prop_id`) VALUES (obj_id,prop);
	FETCH props_cur INTO prop;		
	UNTIL no_more_props = 1
	end repeat;
	CLOSE props_cur;
    END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Структура таблицы `object`
--

CREATE TABLE IF NOT EXISTS `object` (
  `object_id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_object_id` int(11) DEFAULT NULL,
  `object_template_id` int(11) DEFAULT NULL,
  `object_created_at` timestamp NULL DEFAULT NULL,
  `object_updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`object_id`),
  KEY `object_template_id` (`object_template_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=COMPACT AUTO_INCREMENT=6 ;

--
-- Дамп данных таблицы `object`
--

INSERT INTO `object` (`object_id`, `parent_object_id`, `object_template_id`, `object_created_at`, `object_updated_at`) VALUES
(1, NULL, 9, '2011-10-21 11:00:12', '2011-10-21 00:00:00'),
(2, NULL, 11, '2011-10-21 11:03:51', '2011-10-21 00:00:00'),
(3, NULL, 13, '2011-10-21 11:03:51', '2011-10-21 00:00:00'),
(4, NULL, 17, '2011-10-21 11:04:15', '2011-10-21 00:00:00');

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
  `strval` varchar(256) DEFAULT NULL,
  `textval` varchar(500) DEFAULT NULL,
  `dateval` datetime DEFAULT NULL,
  `listval` int(11) DEFAULT NULL,
  PRIMARY KEY (`object_props_id`,`object_id`,`prop_id`),
  KEY `R_1` (`object_id`),
  KEY `R_16` (`prop_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC AUTO_INCREMENT=10 ;

--
-- Дамп данных таблицы `object_props`
--

INSERT INTO `object_props` (`object_props_id`, `object_id`, `prop_id`, `intval`, `floatval`, `strval`, `textval`, `dateval`, `listval`) VALUES
(1, 1, 7, NULL, NULL, 'Главный склад', NULL, NULL, NULL),
(2, 2, 7, NULL, NULL, 'Главный склад возврата', NULL, NULL, NULL),
(3, 3, 7, NULL, NULL, 'Главый каталог накладных', NULL, NULL, NULL),
(4, 4, 7, NULL, NULL, 'Главный каталог продаж', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Структура таблицы `object_prop_list`
--

CREATE TABLE IF NOT EXISTS `object_prop_list` (
  `object_property_list_id` int(11) NOT NULL AUTO_INCREMENT,
  `list_id` int(11) NOT NULL,
  `object_id` int(11) NOT NULL,
  `position` int(11) DEFAULT NULL,
  PRIMARY KEY (`object_property_list_id`,`object_id`),
  KEY `object_id` (`object_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Структура таблицы `object_template`
--

CREATE TABLE IF NOT EXISTS `object_template` (
  `object_template_id` int(11) NOT NULL AUTO_INCREMENT,
  `Object_Template_Name` varchar(20) DEFAULT NULL,
  `parent_object_template_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`object_template_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=COMPACT AUTO_INCREMENT=21 ;

--
-- Дамп данных таблицы `object_template`
--

INSERT INTO `object_template` (`object_template_id`, `Object_Template_Name`, `parent_object_template_id`) VALUES
(1, 'Пользователь', NULL),
(2, 'Хэш', NULL),
(3, '﻿тип ед измерения', NULL),
(4, 'ед измерения', NULL),
(5, 'товар', NULL),
(6, 'кассир', NULL),
(7, 'поставщик', NULL),
(8, 'пользователь', NULL),
(9, 'склад', NULL),
(10, 'товар на складе', NULL),
(11, 'склад возврата', NULL),
(12, 'товар на складе возв', NULL),
(13, 'каталог накладных', NULL),
(14, 'накладная', NULL),
(15, 'товар накладной', NULL),
(16, 'скидка', NULL),
(17, 'каталог продаж', NULL),
(18, 'продажа', NULL),
(19, 'действие', NULL),
(20, 'лог действий', NULL);

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC AUTO_INCREMENT=123 ;

--
-- Дамп данных таблицы `object_template_props`
--

INSERT INTO `object_template_props` (`object_template_id`, `prop_id`, `object_template_props_id`) VALUES
(1, 1, 1),
(1, 2, 3),
(2, 1, 5),
(2, 3, 6),
(3, 4, 65),
(3, 5, 66),
(4, 4, 67),
(4, 8, 68),
(5, 4, 69),
(5, 6, 70),
(5, 7, 71),
(5, 8, 72),
(5, 10, 73),
(5, 11, 74),
(5, 13, 75),
(5, 14, 76),
(5, 15, 77),
(5, 16, 78),
(6, 4, 79),
(6, 17, 80),
(6, 16, 81),
(7, 4, 82),
(7, 5, 83),
(7, 21, 84),
(7, 22, 85),
(7, 23, 86),
(7, 16, 87),
(8, 4, 88),
(8, 17, 89),
(8, 26, 90),
(8, 27, 91),
(9, 7, 92),
(10, 4, 93),
(10, 18, 94),
(10, 12, 95),
(10, 20, 96),
(10, 13, 97),
(11, 7, 98),
(12, 19, 99),
(13, 7, 100),
(14, 4, 101),
(14, 28, 102),
(14, 30, 103),
(14, 24, 104),
(15, 18, 105),
(15, 12, 106),
(16, 4, 107),
(16, 25, 108),
(17, 7, 109),
(18, 28, 110),
(18, 32, 111),
(18, 18, 112),
(18, 12, 113),
(18, 29, 114),
(19, 4, 115),
(19, 31, 116),
(19, 27, 117),
(20, 4, 118),
(20, 28, 119),
(20, 18, 120),
(20, 33, 121),
(20, 32, 122);

-- --------------------------------------------------------

--
-- Структура таблицы `property`
--

CREATE TABLE IF NOT EXISTS `property` (
  `prop_id` int(11) NOT NULL AUTO_INCREMENT,
  `prop_type_id` int(11) NOT NULL,
  `prop_defvalue` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`prop_id`),
  KEY `R_11` (`prop_type_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC AUTO_INCREMENT=34 ;

--
-- Дамп данных таблицы `property`
--

INSERT INTO `property` (`prop_id`, `prop_type_id`, `prop_defvalue`) VALUES
(1, 1, 'Имя'),
(2, 1, 'Фамилия'),
(3, 1, 'хэш'),
(4, 2, '﻿ид'),
(5, 1, 'имя'),
(6, 1, 'штрихкод'),
(7, 1, 'название'),
(8, 2, 'ед.изм'),
(9, 2, 'тип.ед.изм'),
(10, 2, 'мин.кол-во'),
(11, 2, 'предок'),
(12, 2, 'кол-во.товара'),
(13, 5, 'дата.добавления'),
(14, 2, 'цена.приход'),
(15, 2, 'цена.полная'),
(16, 4, 'доп.инфо'),
(17, 1, 'фио'),
(18, 2, 'ид.товара'),
(19, 1, 'причина'),
(20, 1, 'место.на.складе'),
(21, 1, 'адрес'),
(22, 1, 'телефон'),
(23, 1, 'емайл'),
(24, 2, 'поставщик'),
(25, 3, 'проц.скидки'),
(26, 1, 'пароль'),
(27, 2, 'уровень'),
(28, 5, 'дата'),
(29, 3, 'сумма'),
(30, 3, 'номер накладной'),
(31, 2, 'тип.действия'),
(32, 3, 'ид кассира'),
(33, 3, 'ид действия');

-- --------------------------------------------------------

--
-- Структура таблицы `prop_type`
--

CREATE TABLE IF NOT EXISTS `prop_type` (
  `prop_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `prop_type_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`prop_type_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC AUTO_INCREMENT=7 ;

--
-- Дамп данных таблицы `prop_type`
--

INSERT INTO `prop_type` (`prop_type_id`, `prop_type_name`) VALUES
(1, 'Строка'),
(2, 'Целое число'),
(3, 'Дробеое число'),
(4, 'Заметка'),
(5, 'Дата'),
(6, 'Список');

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
-- Ограничения внешнего ключа таблицы `object_prop_list`
--
ALTER TABLE `object_prop_list`
  ADD CONSTRAINT `object_prop_list_ibfk_1` FOREIGN KEY (`object_id`) REFERENCES `object` (`object_id`) ON DELETE CASCADE;

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
