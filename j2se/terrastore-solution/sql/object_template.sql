-- phpMyAdmin SQL Dump
-- version 3.4.5deb1
-- http://www.phpmyadmin.net
--
-- Хост: localhost
-- Время создания: Окт 17 2011 г., 17:35
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
-- Дамп данных таблицы `object_template`
--

INSERT INTO `object_template` (`object_template_id`, `Object_Template_Name`, `parent_object_template_id`) VALUES
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

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
