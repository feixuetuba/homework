/*=========================����=���ͱ�============================*/
create table dlog_message_send 
select 
m.msgid as send_msg_id,									/*���ű��*/
m.from_user_id as send_user_id,								/*�����߱��*/
m.userid as send_r_user_id,								/*�����߱��*/
(select nickname from dlog_user  as u where u.userid=m.userid) as send_r_user_name,	/*����������*/
m.send_time,										/*����ʱ��*/
m.status=-1 as send_status								/*����״̬*/
from dlog_message as m;

/*=============================���ͱ��������===========================*/
create index r_msg_send_fk on dlog_message_send
(
   send_user_id
);

/*=============================��������Զ���������======================*/
alter table dlog_message_send add send_id int
 not null auto_increment ,add primary key (send_id);


/*========================�жϺ���=======================================*/
CREATE FUNCTION getStatus(uid int,fid int,msgid int )
      RETURNS int
BEGIN
      	DECLARE  msgStatus int;								/*����״̬*/				
    	DECLARE countF int;								/*�Ƿ�Ϊ���� 0:���� >0:��*/		
	DECLARE status int;								/*���ز��� ���ձ�״̬*/
	SELECT count(*) INTO countF   FROM   dlog_friend   WHERE   user_id=uid  and  friend_id=fid; 
	SELECT m.status  INTO msgStatus  FROM   dlog_message  as m  WHERE m.msgid=msgid; 

	IF countF = 0  THEN  
       		SET status =msgStatus+3;						/*İ����״̬*/
      	ELSEIF countF > 0 or uid = fid THEN   
  		SET status =msgStatus ;							/*����״̬*/
      	END IF;
      	RETURN ( status );
END;
/* drop FUNCTION getStatus  */
/*=========================����=���ձ�============================*/
create table dlog_message_receiver 
select 
m.msgid as receiver_msg_id,								/*���ű��*/		
m.userid as receiver_user_id,								/*�����߱��*/
m.from_user_id as receiver_s_user_id,							/*�����߱��*/
m.from_user_name as receiver_s_user_name,						/*����������*/
m.send_time as receiver_write_time,							/*ʱ��*/
getStatus(m.userid,m.from_user_id,m.msgid) as receiver_status				/*״̬*/
from dlog_message as m;

/*=========================���ձ��������===========================*/
create index r_msg_send_fk on dlog_message_receiver
(
   receiver_msg_id
);

/*==========================���ձ�����Զ���������==========================*/
alter table dlog_message_receiver add receiver_id int
 not null auto_increment ,add primary key (receiver_id);



/*========================�������ѷ����====================*/
create table dlog_user_f_group
select userid as group_user_id,								/*�����û����*/
 "�ҵĺ���"  as  group_name,								/*�����û�����*/
'2' as group_type,									/*��������*/
'0' as group_count									/*���� 2:ϵͳĬ�� */
 from dlog_user;

/*======================���ѷ����׷������===============================*/
alter table dlog_user_f_group add group_id int
 not null auto_increment ,add primary key (group_id);

/*===============dlog_friend��׷���ֶ�================================*/
alter table dlog_friend add group_id int;

/*=====================�����û������к���ΪĬ�Ϸ���ID===============*/
CREATE function  setGroupID()
returns int
BEGIN
   	DECLARE userCount int;							/*���û���*/
	DECLARE usero int;							/*���û�*/
   	DECLARE uid int;							/*���û����*/
	DECLARE cc int;								/**/
      	SELECT count(*) INTO userCount from dlog_user;				/*���û���*/
	set uid=1;								/*��ʼuserid*/
  	WHILE userCount> 0 DO							/*ѭ��*/	
		select count(*) into usero from dlog_user where userid=uid;	/*�û��Ƿ����*/	
		if usero > 0 then						/*������*/
 			update dlog_friend as f   
			set f.group_id =(select g.group_id  from dlog_user_f_group as g where g.group_user_id=uid and group_type=2)/*�û����˵ķ���id*/
			where f.user_id=uid ;    				/*�޸� group_id*/
      			SET userCount=userCount-1;				/*ѭ�� -1*/
			set uid=uid+1;						/*userid +1*/
		ELSEIF usero <1  THEN   					/*û���û�*/
			set uid=uid+1;						/*userid +1*/
	END IF;
END WHILE; 
return uid;
END;

select setGroupID() as �������û�;

/*==============վ���׷���ֶ�  friend_status friend_cname  =====================*/

alter table dlog_site add friend_status smallint ;

alter table dlog_site add friend_cname varchar(16);



/*==׷��dlog_user���ֶ� portrair_icon ����ʱ�õ���Сͷ��==*/
alter table dlog_user add portrait_icon varchar(100) ;


/*==�ռǱ� ׷���ֶ� annex ==*/
alter table dlog_diary add diary_annex int  default 0;


/*==�û��� ׷���ֶ� popedom==*/
alter table dlog_user add popedom int default 0;



/*==============================================================*/
/* Table: dlog_message_affiche                                  */
/*==============================================================*/
create table dlog_message_affiche
(
   affiche_id           int not null auto_increment,
   affiche_title        varchar(100) not null,
   affiche_content      text not null,
   affiche_send_time    datetime not null,
   affiche_status       int not null,
   affiche_user_id      int,
   primary key (affiche_id)
);

/*==============================================================*/
/* Index: affiche_id                                            */
/*==============================================================*/
create index affiche_id on dlog_message_affiche
(
   affiche_id
);


/*==============================================================*/
/* Table: dlog_diary_annex                                      */
/*==============================================================*/
create table dlog_diary_annex
(
   annex_id             int not null auto_increment,
   annex_user_id        int not null,
   annex_site_id        int not null,
   annex_upload_time    datetime not null,
   annex_download_count char(10) not null,
   annex_status         int,
   annex_validate       bigint not null,
   annex_type           int,
   annex_url            varchar(256) not null,
   annex_disk_path      varchar(256) not null,
   annex_file_name      varchar(256) not null,
   annex_file_desc      varchar(256),
   annex_name           varchar(256) not null,
   annex_diary_id       int,
   annex_size           int not null,
   annex_last_time      datetime,
   annex_extend1        int,
   annex_extend2        int,
   annex_extend3        varchar(1024),
   annex_extend4        varchar(1024),
   primary key (annex_id)
);

/*==============================================================*/
/* Index: annex_id                                              */
/*==============================================================*/
create index annex_id on dlog_diary_annex
(
   annex_id
);


/*==============================================================*/
/* Table: dlog_style                                            */
/*==============================================================*/
create table dlog_style
(
   style_id             int not null auto_increment,
   style_path_name      varchar(200) not null,
   style_name           varchar(200) not null,
   style_css            varchar(200) not null,
   style_preview_l_image varchar(200) not null,
   style_preview_s_image varchar(200) not null,
   style_explain        varchar(1024),
   style_status         int not null,
   style_type           int not null,
   style_level          int not null,
   style_count          int not null,
   style_create_time    datetime,
   style_extend1        int,
   style_extend2        int,
   style_extend3        varchar(1024),
   style_extend4        varchar(1024),
   primary key (style_id)
);

/*==============================================================*/
/* Index: style_id                                              */
/*==============================================================*/
create index style_id on dlog_style
(
   style_id
);


