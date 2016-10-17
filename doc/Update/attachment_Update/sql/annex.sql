create table dlog_diary_annex ( 
	an_id 				int 		not null auto_increment, 
	user_id				int 		not null,
	site_id				int 		not null,
	upload_time	  		datetime	not null,
	count				int		not null,
	status				int,
	validate			bigint		not null,
	type				varchar(32),
	url				varchar(256)	not null,
	disk_path			varchar(256)	not null,
	file_name			varchar(64)	not null,
	file_desc			varchar(128),
	annex_name			varchar(64)	not null,
	diary_id			int,
	file_size			int		not null,
	last_download			datetime,
	extend1				int,
	extend2				int,
	extend3				varchar(16),
	extend4				varchar(16),
	primary key (an_id)
) ;
create index r_annex_id_fk on dlog_diary_annex 
(
   an_id 
);
create index r_annex_user_fk on dlog_diary_annex 
(
   user_id
);