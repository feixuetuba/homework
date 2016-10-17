/*=========================复制=发送表============================*/
create table dlog_message_send 
select 
m.msgid as send_msg_id,									/*短信编号*/
m.from_user_id as send_user_id,								/*发送者编号*/
m.userid as send_r_user_id,								/*接收者编号*/
(select nickname from dlog_user  as u where u.userid=m.userid) as send_r_user_name,	/*接收者名称*/
m.send_time,										/*发送时间*/
m.status=-1 as send_status								/*发送状态*/
from dlog_message as m;

/*=============================发送表添加索引===========================*/
create index r_msg_send_fk on dlog_message_send
(
   send_user_id
);

/*=============================发送添加自动增长主键======================*/
alter table dlog_message_send add send_id int
 not null auto_increment ,add primary key (send_id);


/*========================判断好友=======================================*/
CREATE FUNCTION getStatus(uid int,fid int,msgid int )
      RETURNS int
BEGIN
      	DECLARE  msgStatus int;								/*短信状态*/				
    	DECLARE countF int;								/*是否为好友 0:不是 >0:是*/		
	DECLARE status int;								/*返回参数 接收表状态*/
	SELECT count(*) INTO countF   FROM   dlog_friend   WHERE   user_id=uid  and  friend_id=fid; 
	SELECT m.status  INTO msgStatus  FROM   dlog_message  as m  WHERE m.msgid=msgid; 

	IF countF = 0  THEN  
       		SET status =msgStatus+3;						/*陌生人状态*/
      	ELSEIF countF > 0 or uid = fid THEN   
  		SET status =msgStatus ;							/*好友状态*/
      	END IF;
      	RETURN ( status );
END;
/* drop FUNCTION getStatus  */
/*=========================复制=接收表============================*/
create table dlog_message_receiver 
select 
m.msgid as receiver_msg_id,								/*短信编号*/		
m.userid as receiver_user_id,								/*接收者编号*/
m.from_user_id as receiver_s_user_id,							/*发送者编号*/
m.from_user_name as receiver_s_user_name,						/*发送者名称*/
m.send_time as receiver_write_time,							/*时间*/
getStatus(m.userid,m.from_user_id,m.msgid) as receiver_status				/*状态*/
from dlog_message as m;

/*=========================接收表添加索引===========================*/
create index r_msg_send_fk on dlog_message_receiver
(
   receiver_msg_id
);

/*==========================接收表添加自动增长主键==========================*/
alter table dlog_message_receiver add receiver_id int
 not null auto_increment ,add primary key (receiver_id);



/*========================创建好友分组表====================*/
create table dlog_user_f_group
select userid as group_user_id,								/*所属用户编号*/
 "我的好友"  as  group_name,								/*所属用户名称*/
'2' as group_type,									/*分组名称*/
'0' as group_count									/*类型 2:系统默认 */
 from dlog_user;

/*======================好友分组表追加主键===============================*/
alter table dlog_user_f_group add group_id int
 not null auto_increment ,add primary key (group_id);

/*===============dlog_friend表追加字段================================*/
alter table dlog_friend add group_id int;

/*=====================设置用户的所有好友为默认分组ID===============*/
CREATE function  setGroupID()
returns int
BEGIN
   	DECLARE userCount int;							/*总用户数*/
	DECLARE usero int;							/*总用户*/
   	DECLARE uid int;							/*总用户编号*/
	DECLARE cc int;								/**/
      	SELECT count(*) INTO userCount from dlog_user;				/*总用户数*/
	set uid=1;								/*初始userid*/
  	WHILE userCount> 0 DO							/*循环*/	
		select count(*) into usero from dlog_user where userid=uid;	/*用户是否存在*/	
		if usero > 0 then						/*有数据*/
 			update dlog_friend as f   
			set f.group_id =(select g.group_id  from dlog_user_f_group as g where g.group_user_id=uid and group_type=2)/*用户迷人的分组id*/
			where f.user_id=uid ;    				/*修改 group_id*/
      			SET userCount=userCount-1;				/*循环 -1*/
			set uid=uid+1;						/*userid +1*/
		ELSEIF usero <1  THEN   					/*没有用户*/
			set uid=uid+1;						/*userid +1*/
	END IF;
END WHILE; 
return uid;
END;

select setGroupID() as 共设置用户;

/*==============站点表追加字段  friend_status friend_cname  =====================*/

alter table dlog_site add friend_status smallint ;

alter table dlog_site add friend_cname varchar(16);



/*==追加dlog_user表字段 portrair_icon 评论时用到的小头像==*/
alter table dlog_user add portrait_icon varchar(100) ;


/*==日记表 追加字段 annex ==*/
alter table dlog_diary add diary_annex int  default 0;


/*==用户表 追加字段 popedom==*/
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


