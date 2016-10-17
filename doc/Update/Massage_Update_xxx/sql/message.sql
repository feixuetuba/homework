/*==============================��ṹ=====================*/

/*===���ͱ�===*/
create table dlog_message_send
(
  sendid                          int                            not null auto_increment,
   msgid                          int                            not null,
   userid                         int,
   receiver_user_id               int                            not null,
   receiver_user_name             varchar(50)                    not null,
   send_time                      datetime                       not null,
   status                         smallint                       not null,
 primary key (sendid)
);

create index r_msg_send_fk on dlog_message_send
(
   userid
);

/*===���ձ�===*/
create table dlog_message_receiver
(
receiverid                          int                            not null auto_increment,
   msgid                          int                            not null,
   userid                         int,
   send_user_id                   int                            not null,
   send_user_name                 varchar(50)                    not null, 
   read_time                      datetime                       not null,
   status                         smallint                       not null,
 primary key (receiverid)
);

create index r_msg_receiver1_fk on dlog_message_receiver
(
   userid
);

/*===����ϵͳ��===*/
create table dlog_message_affiche
(
   msgid                          int                            not null auto_increment,
   userid                         int,
   title                          varchar(50)                    not null,
   content                        text                           not null,
   send_time                      datetime                       not null,
   status                         smallint                       not null,
   primary key (msgid)
);






