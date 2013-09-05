CREATE DATABASE simple_chat DEFAULT CHARACTER SET utf8; 

USE simple_chat;

CREATE TABLE dialog (
  id int(11) NOT NULL auto_increment,
  txt text,
  type int(2) NOT NULL,
  date datetime default NULL,
  clientIP varchar(15) default NULL,
  PRIMARY KEY  (id)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='To Recode Dialogs';
