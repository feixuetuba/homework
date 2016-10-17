/*=========================����=���ͱ�============================*/
create table dlog_message_send 
select 
m.msgid,
m.from_user_id as userid,
m.userid as receiver_user_id,
(select nickname from dlog_user  as u where u.userid=m.userid) as receiver_user_name,
m.send_time,
m.status=-1 as status
from dlog_message as m;

/*=============================���ͱ��������===========================*/
create index r_msg_send_fk on dlog_message_send
(
   userid
);

/*=============================��������Զ���������======================*/
alter table dlog_message_send add sendid int
 not null auto_increment ,add primary key (sendid);


/*=========================���Ʊ�=���ձ�============================*/
CREATE FUNCTION getStatus(uid int,fid int,msgid int )
      RETURNS int
BEGIN
      	DECLARE  msgStatus int;
    	DECLARE countF int;
	DECLARE status int;
	SELECT count(*) INTO countF   FROM   dlog_friend   WHERE   user_id=uid  and  friend_id=fid; 
	SELECT m.status  INTO msgStatus  FROM   dlog_message  as m  WHERE m.msgid=msgid; 

	IF countF = 0  THEN  
       		SET status =msgStatus+3;
      	ELSEIF countF > 0 or uid = fid THEN   
  		SET status =msgStatus ;
      	END IF;
      	RETURN ( status );
END;
/* drop FUNCTION getStatus  */

create table dlog_message_receiver 
select 
m.msgid as msgid,
m.userid as userid,
m.from_user_id as send_user_id,
m.from_user_name as send_user_name,
m.send_time as read_time,
getStatus(m.userid,m.from_user_id,m.msgid) as status
from dlog_message as m;

/*=========================���ձ��������===========================*/
create index r_msg_send_fk on dlog_message_receiver
(
   userid
);

/*==========================���ձ�����Զ�������==========================*/
alter table dlog_message_receiver add receiverid int
 not null auto_increment ,add primary key (receiverid);



/*========================���ѷ��������ű�====================*/
create table dlog_user_f_group
select userid ,
 "�ҵĺ���"  as  group_name,
'2' as group_type,
'0' as group_count
 from dlog_user;

/*======================׷������===============================*/
alter table dlog_user_f_group add group_id int
 not null auto_increment ,add primary key (group_id);

/*dlog_friend��׷���ֶ�*/
alter table dlog_friend add group_id int;




/*===========================��ϵ=============================*/
alter table dlog_user_f_group add constraint fk_r_f_group foreign key (user_id)
      references dlog_user (userid) on delete restrict on update restrict;

alter table dlog_friend add constraint fk_r_u_f_group foreign key (group_id)
      references dlog_user_f_group (group_id) on delete restrict on update restrict;


/*=====================�����û������к���ΪĬ�Ϸ���ID===============*/
CREATE function  setGroupID()
returns int
BEGIN
   	DECLARE userCount int;
	DECLARE usero int;
   	DECLARE uid int;
	DECLARE cc int;
      	SELECT count(*) INTO userCount from dlog_user;/*�û���*/
	set uid=1;/*��ʼuserid*/
  	WHILE userCount> 0 DO/*ѭ��*/	
		select count(*) into usero from dlog_user where userid=uid;/*�û��Ƿ����*/	
		if usero > 0 then/*������*/
 			update dlog_friend as f   
			set f.group_id =(select g.group_id  from dlog_user_f_group as g where g.user_id=uid and group_type=2)/*�û�Ĭ�ϵķ���id*/
			where f.user_id=uid ;    /*�޸� group_id*/
      			SET userCount=userCount-1;/*ѭ�� -1*/
			set uid=uid+1;/*userid +1*/
		ELSEIF usero <1  THEN   /*û���û�*/
			set uid=uid+1;/*userid +1*/
	END IF;
END WHILE; 
return uid;
END;

select setGroupID() as �������û�;



/*==============״̬����� ���ѹ��� ׷�������ֶ�=====================*/

alter table dlog_site add friend_status smallint ;

alter table dlog_site add friend_cname varchar(16);



/*==׷��dlog_user���ֶ� portrair_icon ����ʱ�õ���Сͷ��==*/
alter table dlog_user add portrait_icon varchar(100) ;


/*==�ռǱ�==*/
alter table dlog_diary add annex int  ;
update dlog_diary set annex=0;
alter table dlog_diary  change   annex  annex int  not null ;

/*==�û�Ȩ��==*/
alter table dlog_user add popedom int ;
update dlog_user set popedom=0;
alter table dlog_user  change   popedom  popedom int  not null ;


