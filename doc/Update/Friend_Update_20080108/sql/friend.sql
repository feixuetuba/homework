create table dlog_user_f_group ( 
	group_id 	int 		not null auto_increment, 
	user_id 	int 		not null, 
	group_name 	varchar(20) 	not null, 
	group_type	int		not null,
	group_count 	int 		not null,
	primary key (group_id) 
);

alter table dlog_user_f_group add constraint fk_r_f_group foreign key (user_id)
      references dlog_user (userid) on delete restrict on update restrict;




alter table dlog_friend add constraint fk_r_u_f_group foreign key (group_id)
      references dlog_user_f_group (group_id) on delete restrict on update restrict;


