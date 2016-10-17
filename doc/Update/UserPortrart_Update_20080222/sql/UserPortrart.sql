/*==追加dlog_user表字段 portrait_icon 评论时用到的小头像==*/
alter table dlog_user add portrait_icon varchar(50) ;

/*==追加dlog_user表字段 popedom 权限==*/
alter table dlog_user add popedom int ;
update dlog_user set popedom=0;
alter table dlog_user  change   popedom  popedom int  not null ;

/*==追加dlog_user表字段 visitor 访问者(组)==*/
alter table dlog_user add visitor varchar(100) ;

/*==模板样式表==*/
create table dlog_style ( 
	style_id 			int 		not null auto_increment,
	style_name 			varchar(100)	not null,
	style_child_name 			varchar(100)	not null,
	style_css 			varchar(100)	not null,
	style_preview_l_image 		varchar(100)	not null,
	style_preview_s_image		varchar(100)	not null,
	style_explain			varchar(100),
	style_status			int  		not null,
	style_type			int 		not null,
	style_level			int 		not null,	
	style_count			int		not null,
	style_vip			varchar(200),
	style_create_time		datetime,
	extend1				int,
	extend2				int,
	extend3				varchar(200),
	extend4				varchar(200),
	primary key (style_id) 
) ;
/*==索引==*/
create index r_style_id_fk on dlog_style 
(
   style_id 
);


