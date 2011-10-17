-- phpMyAdmin SQL Dump
-- version 3.4.5deb1
-- http://www.phpmyadmin.net
--
-- Хост: localhost
-- Время создания: Окт 17 2011 г., 14:54
-- Версия сервера: 5.1.58
-- Версия PHP: 5.3.8-2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

-- Дамп данных таблицы `property`
--

INSERT INTO `property` (`prop_id`, `prop_type_id`, `prop_defvalue`) VALUES
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
(24, 2, 'ид поставщика'),
(25, 3, 'проц.скидки'),
(26, 1, 'пароль'),
(27, 2, 'уровень'),
(28, 5, 'дата'),
(29, 3, 'сумма'),
(30, 3, 'номер накладной'),
(31, 2, 'тип.действия'),
(32, 3, 'ид кассира'),
(33, 3, 'ид действия');




/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
