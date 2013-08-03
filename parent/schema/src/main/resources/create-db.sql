create database trabajo;
use trabajo;
insert into tuser (username, password, createdOn, locale, loginRole, langPref, full_name, title, primaryemail) values ('admin', 'admin', '2012-09-25T14:00:00', 'en', 'default', 'en', 'Colin Thorburn', 'Mr', 'cthorburn8@gmail.com')insert into tuser (user_id, username, password, createdOn, locale, loginRole, langPref, full_name, title, primaryemail) values (1, 'colin', 'colin', '2012-09-25T14:00:00', 'en', 'default', 'en', 'Colin Thorburn', 'Mr', 'cthorburn8@gmail.com')
insert into tuser (username, password, createdOn, locale, loginRole, langPref, full_name, title, primaryemail) values ('colin', 'colin', '2012-09-25T14:00:00', 'en', 'default', 'en', 'Colin Thorburn', 'Mr', 'cthorburn8@gmail.com')
grant all privileges on trabajo to colin@localhost identified by 'colin'
