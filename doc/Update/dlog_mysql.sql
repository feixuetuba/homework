/*==============================================================*/
/* DBMS name:      MySQL 4.1.18 or later                        */
/* Created on:     2006-8-16 23:07:23                           */
/*==============================================================*/


/*==============================================================*/
/* Table: dlog_album                                            */
/*==============================================================*/
create table dlog_album
(
   album_id                       int                            not null auto_increment,
   photo_id                       int,
   dlo_album_id                   int,
   site_id                        int                            not null,
   dlog_type_id                   int,
   album_name                     varchar(40)                    not null,
   album_desc                     varchar(200),
   photo_count                    int                            not null,
   album_type                     int                            not null,
   verifycode                     varchar(20),
   sort_order                     int                            not null,
   create_time                    datetime                       not null,
   primary key (album_id)
)
type = innodb;

/*==============================================================*/
/* Index: r_site_album_fk                                       */
/*==============================================================*/
create index r_site_album_fk on dlog_album
(
   site_id
);

/*==============================================================*/
/* Index: r_album_fk                                            */
/*==============================================================*/
create index r_album_fk on dlog_album
(
   dlo_album_id
);

/*==============================================================*/
/* Index: r_album_type_fk                                       */
/*==============================================================*/
create index r_album_type_fk on dlog_album
(
   dlog_type_id
);

/*==============================================================*/
/* Index: r_album_cover_fk                                      */
/*==============================================================*/
create index r_album_cover_fk on dlog_album
(
   photo_id
);

/*==============================================================*/
/* Table: dlog_blocked_ip                                       */
/*==============================================================*/
create table dlog_blocked_ip
(
   blocked_ip_id                  int                            not null auto_increment,
   site_id                        int,
   ip_addr                        int                            not null,
   s_ip_addr                      varchar(16)                    not null,
   ip_mask                        int                            not null,
   s_ip_mask                      varchar(16)                    not null,
   blocked_type                   smallint                       not null,
   blocked_time                   datetime                       not null,
   status                         smallint                       not null,
   primary key (blocked_ip_id)
)
type = innodb;

/*==============================================================*/
/* Index: r_site_blocked_ip_fk                                  */
/*==============================================================*/
create index r_site_blocked_ip_fk on dlog_blocked_ip
(
   site_id
);

/*==============================================================*/
/* Table: dlog_bookmark                                         */
/*==============================================================*/
create table dlog_bookmark
(
   mark_id                        int                            not null auto_increment,
   userid                         int                            not null,
   site_id                        int                            not null,
   parent_id                      int                            not null,
   parent_type                    smallint                       not null,
   title                          varchar(200),
   url                            varchar(200),
   create_time                    datetime                       not null,
   primary key (mark_id)
)
type = innodb;

/*==============================================================*/
/* Index: r_user_mark_fk                                        */
/*==============================================================*/
create index r_user_mark_fk on dlog_bookmark
(
   userid
);

/*==============================================================*/
/* Index: r_site_bookmark_fk                                    */
/*==============================================================*/
create index r_site_bookmark_fk on dlog_bookmark
(
   site_id
);

/*==============================================================*/
/* Table: dlog_bulletin                                         */
/*==============================================================*/
create table dlog_bulletin
(
   bulletin_id                    int                            not null auto_increment,
   site_id                        int                            not null,
   bulletin_type                  int                            not null,
   pub_time                       datetime                       not null,
   status                         smallint                       not null,
   title                          varchar(200)                   not null,
   content                        text                           not null,
   primary key (bulletin_id)
)
type = innodb;

/*==============================================================*/
/* Index: r_site_bulletin_fk                                    */
/*==============================================================*/
create index r_site_bulletin_fk on dlog_bulletin
(
   site_id
);

/*==============================================================*/
/* Table: dlog_catalog                                          */
/*==============================================================*/
create table dlog_catalog
(
   catalog_id                     int                            not null auto_increment,
   dlog_type_id                   int,
   site_id                        int                            not null,
   catalog_name                   varchar(20)                    not null,
   catalog_desc                   varchar(200),
   create_time                    datetime                       not null,
   article_count                  int                            not null,
   catalog_type                   smallint                       not null,
   verifycode                     varchar(20),
   sort_order                     int                            not null,
   primary key (catalog_id)
)
type = innodb;

/*==============================================================*/
/* Index: r_site_catalog_fk                                     */
/*==============================================================*/
create index r_site_catalog_fk on dlog_catalog
(
   site_id
);

/*==============================================================*/
/* Index: r_catalog_type_fk                                     */
/*==============================================================*/
create index r_catalog_type_fk on dlog_catalog
(
   dlog_type_id
);

/*==============================================================*/
/* Table: dlog_catalog_perm                                     */
/*==============================================================*/
create table dlog_catalog_perm
(
   catalog_id                     int                            not null,
   userid                         int                            not null,
   user_role                      int                            not null,
   primary key (catalog_id, userid)
)
type = innodb;

/*==============================================================*/
/* Index: dlog_catalog_perm_fk                                  */
/*==============================================================*/
create index dlog_catalog_perm_fk on dlog_catalog_perm
(
   catalog_id
);

/*==============================================================*/
/* Index: dlog_catalog_perm2_fk                                 */
/*==============================================================*/
create index dlog_catalog_perm2_fk on dlog_catalog_perm
(
   userid
);

/*==============================================================*/
/* Table: dlog_comments                                         */
/*==============================================================*/
create table dlog_comments
(
   comment_id                     int                            not null auto_increment,
   dlo_comment_id                 int,
   site_id                        int,
   entity_id                      int                            not null,
   entity_type                    int                            not null,
   client_ip                      varchar(16)                    not null,
   client_type                    smallint                       not null,
   client_user_agent              varchar(100),
   author_id                      int                            not null,
   author                         varchar(20)                    not null,
   author_email                   varchar(50),
   author_url                     varchar(100),
   title                          varchar(200)                   not null,
   content                        text                           not null,
   content_format                 smallint                       not null,
   comment_time                   datetime                       not null,
   comment_flag                   smallint                       not null,
   comment_status                 smallint                       not null,
   primary key (comment_id)
)
type = innodb;

/*==============================================================*/
/* Index: r_sub_comment_fk                                      */
/*==============================================================*/
create index r_sub_comment_fk on dlog_comments
(
   dlo_comment_id
);

/*==============================================================*/
/* Index: r_site_comment_fk                                     */
/*==============================================================*/
create index r_site_comment_fk on dlog_comments
(
   site_id
);

/*==============================================================*/
/* Table: dlog_config                                           */
/*==============================================================*/
create table dlog_config
(
   config_id                      int                            not null auto_increment,
   site_id                        int,
   config_name                    varchar(20)                    not null,
   int_value                      int,
   string_value                   varchar(100),
   date_value                     date,
   time_value                     time,
   timestamp_value                datetime,
   last_update                    timestamp                      not null,
   primary key (config_id)
)
type = innodb;

/*==============================================================*/
/* Index: r_site_config_fk                                      */
/*==============================================================*/
create index r_site_config_fk on dlog_config
(
   site_id
);

/*==============================================================*/
/* Table: dlog_diary                                            */
/*==============================================================*/
create table dlog_diary
(
   diary_id                       int                            not null auto_increment,
   userid                         int                            not null,
   site_id                        int                            not null,
   catalog_id                     int                            not null,
   author                         varchar(20)                    not null,
   author_url                     varchar(100),
   title                          varchar(200)                   not null,
   content                        text                           not null,
   diary_size                     int                            not null,
   refer                          varchar(100),
   weather                        varchar(20)                    not null,
   mood_level                     smallint                       not null,
   tags                           varchar(100),
   bgsound                        int,
   annex                          int,
   reply_count                    int                            not null,
   view_count                     int                            not null,
   tb_count                       int                            not null,
   client_type                    smallint                       not null,
   client_ip                      varchar(16)                    not null,
   client_user_agent              varchar(100),
   write_time                     datetime                       not null,
   last_read_time                 datetime,
   last_reply_time                datetime,
   modify_time                    datetime,
   reply_notify                   smallint                       not null,
   diary_type                     smallint                       not null,
   locked                         smallint                       not null,
   status                         smallint                       not null,
   primary key (diary_id)
)
type = innodb;

/*==============================================================*/
/* Index: r_site_journal_fk                                     */
/*==============================================================*/
create index r_site_journal_fk on dlog_diary
(
   site_id
);

/*==============================================================*/
/* Index: r_catalog_diary_fk                                    */
/*==============================================================*/
create index r_catalog_diary_fk on dlog_diary
(
   catalog_id
);

/*==============================================================*/
/* Index: r_user_journal_fk                                     */
/*==============================================================*/
create index r_user_journal_fk on dlog_diary
(
   userid
);

/*==============================================================*/
/* Table: dlog_external_refer                                   */
/*==============================================================*/
create table dlog_external_refer
(
   refer_id                       int                            not null auto_increment,
   site_id                        int,
   ref_id                         int                            not null,
   ref_type                       smallint                       not null,
   refer_host                     varchar(50),
   refer_url                      varchar(250)                   not null,
   client_ip                      varchar(16)                    not null,
   refer_time                     datetime                       not null,
   primary key (refer_id)
)
type = innodb;

/*==============================================================*/
/* Index: r_site_refer_fk                                       */
/*==============================================================*/
create index r_site_refer_fk on dlog_external_refer
(
   site_id
);

/*==============================================================*/
/* Table: dlog_fck_upload_file                                  */
/*==============================================================*/
create table dlog_fck_upload_file
(
   fck_file_id                    int                            not null auto_increment,
   userid                         int                            not null,
   site_id                        int,
   upload_time                    datetime                       not null,
   session_id                     varchar(100)                   not null,
   ref_id                         int                            not null,
   ref_type                       smallint                       not null,
   save_path                      varchar(255)                   not null,
   file_uri                       varchar(100)                   not null,
   file_type                      int                            not null,
   file_size                      int                            not null,
   primary key (fck_file_id)
)
type = innodb;

/*==============================================================*/
/* Index: r_site_file_fk                                        */
/*==============================================================*/
create index r_site_file_fk on dlog_fck_upload_file
(
   site_id
);

/*==============================================================*/
/* Index: r_user_upload_fk                                      */
/*==============================================================*/
create index r_user_upload_fk on dlog_fck_upload_file
(
   userid
);

/*==============================================================*/
/* Table: dlog_forum                                            */
/*==============================================================*/
create table dlog_forum
(
   forum_id                       int                            not null auto_increment,
   site_id                        int                            not null,
   dlog_type_id                   int,
   forum_name                     varchar(40)                    not null,
   forum_desc                     varchar(200),
   forum_type                     int                            not null,
   create_time                    datetime                       not null,
   modify_time                    datetime,
   last_time                      datetime,
   last_user_id                   int,
   last_user_name                 varchar(50),
   last_topic_id                  int,
   sort_order                     int                            not null,
   topic_count                    int                            not null,
   forum_option                   int                            not null,
   status                         smallint                       not null,
   primary key (forum_id)
)
type = innodb;

/*==============================================================*/
/* Index: r_site_forum_fk                                       */
/*==============================================================*/
create index r_site_forum_fk on dlog_forum
(
   site_id
);

/*==============================================================*/
/* Index: r_forum_type_fk                                       */
/*==============================================================*/
create index r_forum_type_fk on dlog_forum
(
   dlog_type_id
);

/*==============================================================*/
/* Table: dlog_friend                                           */
/*==============================================================*/
create table dlog_friend
(
   user_id                        int                            not null,
   friend_id                      int                            not null,
   friend_type                    int                            not null,
   friend_role                    int                            not null,
   add_time                       datetime                       not null,
   group_id                       int                            not null,
   primary key (user_id, friend_id)
)
type = innodb;

/*==============================================================*/
/* Table: dlog_guestbook                                        */
/*==============================================================*/
create table dlog_guestbook
(
   guest_book_id                  int                            not null auto_increment,
   userid                         int                            not null,
   site_id                        int                            not null,
   content                        text                           not null,
   client_type                    smallint                       not null,
   client_ip                      varchar(16)                    not null,
   client_user_agent              varchar(100),
   create_time                    datetime                       not null,
   reply_content                  text,
   reply_time                     datetime,
   primary key (guest_book_id)
)
type = innodb;

/*==============================================================*/
/* Index: r_site_guest_fk                                       */
/*==============================================================*/
create index r_site_guest_fk on dlog_guestbook
(
   site_id
);

/*==============================================================*/
/* Index: r_user_liuyan_fk                                      */
/*==============================================================*/
create index r_user_liuyan_fk on dlog_guestbook
(
   userid
);

/*==============================================================*/
/* Table: dlog_j_reply                                          */
/*==============================================================*/
create table dlog_j_reply
(
   j_reply_id                     int                            not null auto_increment,
   site_id                        int                            not null,
   userid                         int,
   diary_id                       int                            not null,
   author                         varchar(20)                    not null,
   author_url                     varchar(100),
   author_email                   varchar(50),
   client_type                    smallint                       not null,
   client_ip                      varchar(16)                    not null,
   client_user_agent              varchar(100),
   owner_only                     int                            not null,
   content                        text                           not null,
   write_time                     datetime                       not null,
   status                         smallint                       not null,
   primary key (j_reply_id)
)
type = innodb;

/*==============================================================*/
/* Index: r_site_j_reply_fk                                     */
/*==============================================================*/
create index r_site_j_reply_fk on dlog_j_reply
(
   site_id
);

/*==============================================================*/
/* Index: r_journal_reply_fk                                    */
/*==============================================================*/
create index r_journal_reply_fk on dlog_j_reply
(
   diary_id
);

/*==============================================================*/
/* Index: r_user_j_reply_fk                                     */
/*==============================================================*/
create index r_user_j_reply_fk on dlog_j_reply
(
   userid
);

/*==============================================================*/
/* Table: dlog_link                                             */
/*==============================================================*/
create table dlog_link
(
   linkid                         int                            not null auto_increment,
   site_id                        int                            not null,
   link_title                     varchar(40)                    not null,
   link_url                       varchar(200)                   not null,
   link_type                      smallint                       not null,
   sort_order                     int                            not null,
   create_time                    datetime                       not null,
   status                         smallint                       not null,
   primary key (linkid)
)
type = innodb;

/*==============================================================*/
/* Index: r_site_link_fk                                        */
/*==============================================================*/
create index r_site_link_fk on dlog_link
(
   site_id
);

/*==============================================================*/
/* Table: dlog_message                                          */
/*==============================================================*/
create table dlog_message
(
   msgid                          int                            not null auto_increment,
   userid                         int,
   from_user_id                   int                            not null,
   from_user_name                 varchar(50)                    not null,
   content                        text                           not null,
   send_time                      datetime                       not null,
   expire_time                    datetime,
   read_time                      datetime,
   status                         smallint                       not null,
   primary key (msgid)
)
type = innodb;

/*==============================================================*/
/* Index: r_msg_receiver_fk                                     */
/*==============================================================*/
create index r_msg_receiver_fk on dlog_message
(
   userid
);

/*==============================================================*/
/* Table: dlog_music                                            */
/*==============================================================*/
create table dlog_music
(
   music_id                       int                            not null auto_increment,
   music_box_id                   int,
   userid                         int,
   site_id                        int                            not null,
   music_title                    varchar(100)                   not null,
   music_word                     text,
   album                          varchar(100),
   singer                         varchar(50),
   url                            varchar(200),
   create_time                    datetime                       not null,
   view_count                     int                            not null,
   music_type                     int                            not null,
   status                         smallint                       not null,
   primary key (music_id)
)
type = innodb;

/*==============================================================*/
/* Index: r_site_music_fk                                       */
/*==============================================================*/
create index r_site_music_fk on dlog_music
(
   site_id
);

/*==============================================================*/
/* Index: r_music_box_fk                                        */
/*==============================================================*/
create index r_music_box_fk on dlog_music
(
   music_box_id
);

/*==============================================================*/
/* Index: r_recommend_fk                                        */
/*==============================================================*/
create index r_recommend_fk on dlog_music
(
   userid
);

/*==============================================================*/
/* Table: dlog_musicbox                                         */
/*==============================================================*/
create table dlog_musicbox
(
   music_box_id                   int                            not null auto_increment,
   site_id                        int                            not null,
   box_name                       varchar(40)                    not null,
   box_desc                       varchar(100),
   music_count                    int                            not null,
   create_time                    datetime                       not null,
   sort_order                     int                            not null,
   primary key (music_box_id)
)
type = innodb;

/*==============================================================*/
/* Index: r_site_mbox_fk                                        */
/*==============================================================*/
create index r_site_mbox_fk on dlog_musicbox
(
   site_id
);

/*==============================================================*/
/* Table: dlog_my_blacklist                                     */
/*==============================================================*/
create table dlog_my_blacklist
(
   my_user_id                     int                            not null,
   other_user_id                  int                            not null,
   bl_type                        int                            not null,
   add_time                       datetime                       not null,
   primary key (my_user_id, other_user_id)
)
type = innodb;

/*==============================================================*/
/* Table: dlog_p_reply                                          */
/*==============================================================*/
create table dlog_p_reply
(
   p_reply_id                     int                            not null auto_increment,
   site_id                        int                            not null,
   photo_id                       int                            not null,
   userid                         int,
   author                         varchar(20)                    not null,
   author_url                     varchar(100),
   author_email                   varchar(50),
   client_type                    smallint                       not null,
   client_ip                      varchar(16)                    not null,
   client_user_agent              varchar(100),
   owner_only                     int                            not null,
   content                        text                           not null,
   write_time                     datetime                       not null,
   status                         smallint                       not null,
   primary key (p_reply_id)
)
type = innodb;

/*==============================================================*/
/* Index: r_photo_reply_fk                                      */
/*==============================================================*/
create index r_photo_reply_fk on dlog_p_reply
(
   photo_id
);

/*==============================================================*/
/* Index: r_site_p_reply_fk                                     */
/*==============================================================*/
create index r_site_p_reply_fk on dlog_p_reply
(
   site_id
);

/*==============================================================*/
/* Index: r_user_p_reply_fk                                     */
/*==============================================================*/
create index r_user_p_reply_fk on dlog_p_reply
(
   userid
);

/*==============================================================*/
/* Table: dlog_photo                                            */
/*==============================================================*/
create table dlog_photo
(
   photo_id                       int                            not null auto_increment,
   site_id                        int                            not null,
   album_id                       int                            not null,
   userid                         int                            not null,
   photo_name                     varchar(40)                    not null,
   photo_desc                     text,
   file_name                      varchar(100)                   not null,
   photo_url                      varchar(100)                   not null,
   preview_url                    varchar(100)                   not null,
   tags                           varchar(100),
   p_year                         int                            not null,
   p_month                        smallint                       not null,
   p_date                         smallint                       not null,
   width                          int                            not null,
   height                         int                            not null,
   photo_size                     bigint                         not null,
   color_bit                      int                            not null,
   exif_manufacturer              varchar(50),
   exif_model                     varchar(50),
   exif_iso                       int                            not null,
   exif_aperture                  varchar(20),
   exif_shutter                   varchar(20),
   exif_exposure_bias             varchar(20),
   exif_exposure_time             varchar(20),
   exif_focal_length              varchar(20),
   exif_color_space               varchar(20),
   reply_count                    int                            not null,
   view_count                     int                            not null,
   create_time                    datetime                       not null,
   modify_time                    datetime,
   last_reply_time                datetime,
   photo_type                     int                            not null,
   locked                         smallint                       not null,
   photo_status                   smallint                       not null,
   primary key (photo_id)
)
type = innodb;

/*==============================================================*/
/* Index: r_album_photo_fk                                      */
/*==============================================================*/
create index r_album_photo_fk on dlog_photo
(
   album_id
);

/*==============================================================*/
/* Index: r_site_photo_fk                                       */
/*==============================================================*/
create index r_site_photo_fk on dlog_photo
(
   site_id
);

/*==============================================================*/
/* Index: r_photo_owner_fk                                      */
/*==============================================================*/
create index r_photo_owner_fk on dlog_photo
(
   userid
);

/*==============================================================*/
/* Table: dlog_site                                             */
/*==============================================================*/
create table dlog_site
(
   site_id                        int                            not null auto_increment,
   userid                         int                            not null,
   dlog_type_id                   int,
   site_name                      varchar(20)                    not null,
   site_c_name                    varchar(50)                    not null,
   site_url                       varchar(100),
   site_title                     varchar(100),
   site_detail                    varchar(250),
   site_icp                       varchar(20),
   site_logo                      varchar(50),
   site_css                       varchar(50),
   site_layout                    varchar(50),
   site_lang                      varchar(10),
   site_flag                      int                            not null,
   create_time                    datetime                       not null,
   last_time                      datetime,
   expired_time                   datetime,
   last_exp_time                  datetime,
   access_mode                    smallint                       not null,
   access_code                    varchar(50),
   diary_status                   smallint                       not null,
   photo_status                   smallint                       not null,
   music_status                   smallint                       not null,
   forum_status                   smallint                       not null,
   friend_status                  smallint                       not null,
   guestbook_status               smallint                       not null,
   diary_cname                    varchar(16),
   photo_cname                    varchar(16),
   music_cname                    varchar(16),
   bbs_cname                      varchar(16),
   guestbook_cname                varchar(16),
   friend_cname                   varchar(16),
   photo_space_total              int                            not null,
   photo_space_used               int                            not null,
   diary_space_total              int                            not null,
   diary_space_used               int                            not null,
   media_space_total              int                            not null,
   media_space_used               int                            not null,
   site_type                      int                            not null,
   site_level                     int                            not null,
   site_ext1                      int                            not null default 0,
   site_ext2                      int                            not null default 0,
   site_ext3                      varchar(16),
   site_ext4                      varchar(16),
   status                         smallint                       not null,
   primary key (site_id)
)
type = innodb;

/*==============================================================*/
/* Index: r_user_site_fk                                        */
/*==============================================================*/
create index r_user_site_fk on dlog_site
(
   userid
);

create index idx_sitename on dlog_site
(
   site_name
);

create index idx_sitecname on dlog_site
(
   site_c_name
);

/*==============================================================*/
/* Index: r_site_type_fk                                        */
/*==============================================================*/
create index r_site_type_fk on dlog_site
(
   dlog_type_id
);

/*==============================================================*/
/* Table: dlog_site_stat                                        */
/*==============================================================*/
create table dlog_site_stat
(
   site_stat_id                   int                            not null auto_increment,
   site_id                        int,
   stat_date                      int                            not null,
   uv_count                       int                            not null,
   pv_count                       int                            not null,
   v_source                       int                            not null,
   update_time                    datetime                       not null,
   primary key (site_stat_id)
)
type = innodb;

/*==============================================================*/
/* Index: r_site_stat_fk                                        */
/*==============================================================*/
create index r_site_stat_fk on dlog_site_stat
(
   site_id
);
create index idx_site_stat on dlog_site_stat
(
   site_id, stat_date
);

/*==============================================================*/
/* Table: dlog_t_reply                                          */
/*==============================================================*/
create table dlog_t_reply
(
   t_reply_id                     int                            not null auto_increment,
   topic_id                       int                            not null,
   userid                         int                            not null,
   site_id                        int                            not null,
   title                          varchar(200)                   not null,
   content                        text                           not null,
   write_time                     datetime,
   status                         smallint,
   client_ip                      varchar(16),
   client_type                    smallint,
   client_user_agent              varchar(100),
   primary key (t_reply_id)
)
type = innodb;

/*==============================================================*/
/* Index: r_site_t_reply_fk                                     */
/*==============================================================*/
create index r_site_t_reply_fk on dlog_t_reply
(
   site_id
);

/*==============================================================*/
/* Index: r_topic_reply_fk                                      */
/*==============================================================*/
create index r_topic_reply_fk on dlog_t_reply
(
   topic_id
);

/*==============================================================*/
/* Index: r_user_t_reply_fk                                     */
/*==============================================================*/
create index r_user_t_reply_fk on dlog_t_reply
(
   userid
);

/*==============================================================*/
/* Table: dlog_tag                                              */
/*==============================================================*/
create table dlog_tag
(
   tag_id                         int                            not null auto_increment,
   site_id                        int                            not null,
   ref_id                         int                            not null,
   ref_type                       smallint                       not null,
   ref_time                       int                            not null,
   tag_name                       varchar(20)                    not null,
   primary key (tag_id)
)
type = innodb;

/*==============================================================*/
/* Index: r_site_tag_fk                                         */
/*==============================================================*/
create index r_site_tag_fk on dlog_tag
(
   site_id
);

create index idx_tagname on dlog_tag
(
   tag_name
);

/*==============================================================*/
/* Table: dlog_topic                                            */
/*==============================================================*/
create table dlog_topic
(
   topic_id                       int                            not null auto_increment,
   site_id                        int                            not null,
   userid                         int                            not null,
   forum_id                       int                            not null,
   username                       varchar(20)                    not null,
   create_time                    datetime                       not null,
   modify_time                    datetime,
   title                          varchar(200)                   not null,
   content                        text                           not null,
   tags                           varchar(100),
   last_reply_time                datetime,
   last_reply_id                  int,
   last_user_id                   int,
   last_user_name                 varchar(50),
   reply_count                    int                            not null,
   view_count                     int                            not null,
   locked                         smallint                       not null,
   topic_type                     int                            not null,
   status                         smallint                       not null,
   client_type                    smallint                       not null,
   client_ip                      varchar(16)                    not null,
   client_user_agent              varchar(100),
   primary key (topic_id)
)
type = innodb;

/*==============================================================*/
/* Index: r_site_topic_fk                                       */
/*==============================================================*/
create index r_site_topic_fk on dlog_topic
(
   site_id
);

/*==============================================================*/
/* Index: r_forum_topic_fk                                      */
/*==============================================================*/
create index r_forum_topic_fk on dlog_topic
(
   forum_id
);

/*==============================================================*/
/* Index: r_user_topic_fk                                       */
/*==============================================================*/
create index r_user_topic_fk on dlog_topic
(
   userid
);

/*==============================================================*/
/* Table: dlog_trackback                                        */
/*==============================================================*/
create table dlog_trackback
(
   track_id                       int                            not null auto_increment,
   parent_id                      int                            not null,
   parent_type                    smallint                       not null,
   refurl                         varchar(100)                   not null,
   title                          varchar(200),
   excerpt                        varchar(200),
   blog_name                      varchar(50)                    not null,
   remote_addr                    char(15)                       not null,
   track_time                     datetime                       not null,
   primary key (track_id)
)
type = innodb;

/*==============================================================*/
/* Table: dlog_type                                             */
/*==============================================================*/
create table dlog_type
(
   dlog_type_id                   int                            not null auto_increment,
   dlo_dlog_type_id               int,
   type_name                      varchar(20)                    not null,
   sort_order                     int                            not null,
   primary key (dlog_type_id)
)
type = innodb;

/*==============================================================*/
/* Index: r_site_type_tree_fk                                   */
/*==============================================================*/
create index r_site_type_tree_fk on dlog_type
(
   dlo_dlog_type_id
);

/*==============================================================*/
/* Table: dlog_user                                             */
/*==============================================================*/
create table dlog_user
(
   userid                         int                            not null auto_increment,
   site_id                        int,
   own_site_id                    int                            not null,
   username                       varchar(20)                    not null,
   password                       varchar(50)                    not null,
   user_role                      int                            not null,
   nickname                       varchar(50)                    not null,
   sex                            smallint                       not null,
   birth                          date,
   email                          varchar(50),
   homepage                       varchar(50),
   qq                             varchar(16),
   msn                            varchar(50),
   mobile                         varchar(16),
   nation                         varchar(40),
   province                       varchar(40),
   city                           varchar(40),
   industry                       varchar(40),
   company                        varchar(80),
   address                        varchar(200),
   job                            varchar(40),
   fax                            varchar(20),
   tel                            varchar(20),
   zip                            varchar(12),
   portrait                       varchar(100),
   portrait_icon                  varchar(100),
   resume                         varchar(200),
   regtime                        datetime                       not null,
   last_time                      datetime,
   last_ip                        varchar(16),
   keep_days                      int                            not null,
   online_status                  int                            not null,
   user_level                     int                            not null,
   status                         smallint                       not null,
   article_count                  int                            not null,
   article_reply_count            int                            not null,
   topic_count                    int                            not null,
   topic_reply_count              int                            not null,
   photo_count                    int                            not null,
   photo_reply_count              int                            not null,
   guestbook_count                int                            not null,
   bookmark_count                 int                            not null,
   user_ext1                      int                            not null default 0,
   user_ext2                      int                            not null default 0,
   user_ext3                      varchar(16),
   user_ext4                      varchar(16),
   primary key (userid)
)
type = innodb;

/*==============================================================*/
/* Index: r_site_user_fk                                        */
/*==============================================================*/
create index r_site_user_fk on dlog_user
(
   site_id
);
create index idx_username on dlog_user
(
   username
);
create index idx_nickname on dlog_user
(
   nickname
);

alter table dlog_album add constraint fk_r_album foreign key (dlo_album_id)
      references dlog_album (album_id) on delete restrict on update restrict;

alter table dlog_album add constraint fk_r_album_cover foreign key (photo_id)
      references dlog_photo (photo_id) on delete restrict on update restrict;

alter table dlog_album add constraint fk_r_album_type foreign key (dlog_type_id)
      references dlog_type (dlog_type_id) on delete restrict on update restrict;

alter table dlog_album add constraint fk_r_site_album foreign key (site_id)
      references dlog_site (site_id) on delete restrict on update restrict;

alter table dlog_blocked_ip add constraint fk_r_site_blocked_ip foreign key (site_id)
      references dlog_site (site_id) on delete restrict on update restrict;

alter table dlog_bookmark add constraint fk_r_site_bookmark foreign key (site_id)
      references dlog_site (site_id) on delete restrict on update restrict;

alter table dlog_bookmark add constraint fk_r_user_mark foreign key (userid)
      references dlog_user (userid) on delete restrict on update restrict;

alter table dlog_bulletin add constraint fk_r_site_bulletin foreign key (site_id)
      references dlog_site (site_id) on delete restrict on update restrict;

alter table dlog_catalog add constraint fk_r_catalog_type foreign key (dlog_type_id)
      references dlog_type (dlog_type_id) on delete restrict on update restrict;

alter table dlog_catalog add constraint fk_r_site_catalog foreign key (site_id)
      references dlog_site (site_id) on delete restrict on update restrict;

alter table dlog_catalog_perm add constraint fk_dlog_catalog_perm foreign key (catalog_id)
      references dlog_catalog (catalog_id) on delete restrict on update restrict;

alter table dlog_catalog_perm add constraint fk_dlog_catalog_perm2 foreign key (userid)
      references dlog_user (userid) on delete restrict on update restrict;

alter table dlog_comments add constraint fk_r_site_comment foreign key (site_id)
      references dlog_site (site_id) on delete restrict on update restrict;

alter table dlog_comments add constraint fk_r_sub_comment foreign key (dlo_comment_id)
      references dlog_comments (comment_id) on delete restrict on update restrict;

alter table dlog_config add constraint fk_r_site_config foreign key (site_id)
      references dlog_site (site_id) on delete restrict on update restrict;

alter table dlog_diary add constraint fk_r_catalog_diary foreign key (catalog_id)
      references dlog_catalog (catalog_id) on delete restrict on update restrict;

alter table dlog_diary add constraint fk_r_site_journal foreign key (site_id)
      references dlog_site (site_id) on delete restrict on update restrict;

alter table dlog_diary add constraint fk_r_user_journal foreign key (userid)
      references dlog_user (userid) on delete restrict on update restrict;

alter table dlog_external_refer add constraint fk_r_site_refer foreign key (site_id)
      references dlog_site (site_id) on delete restrict on update restrict;

alter table dlog_fck_upload_file add constraint fk_r_site_file foreign key (site_id)
      references dlog_site (site_id) on delete restrict on update restrict;

alter table dlog_fck_upload_file add constraint fk_r_user_upload foreign key (userid)
      references dlog_user (userid) on delete restrict on update restrict;

alter table dlog_forum add constraint fk_r_forum_type foreign key (dlog_type_id)
      references dlog_type (dlog_type_id) on delete restrict on update restrict;

alter table dlog_forum add constraint fk_r_site_forum foreign key (site_id)
      references dlog_site (site_id) on delete restrict on update restrict;

alter table dlog_guestbook add constraint fk_r_site_guest foreign key (site_id)
      references dlog_site (site_id) on delete restrict on update restrict;

alter table dlog_guestbook add constraint fk_r_user_liuyan foreign key (userid)
      references dlog_user (userid) on delete restrict on update restrict;

alter table dlog_j_reply add constraint fk_r_journal_reply foreign key (diary_id)
      references dlog_diary (diary_id) on delete restrict on update restrict;

alter table dlog_j_reply add constraint fk_r_site_j_reply foreign key (site_id)
      references dlog_site (site_id) on delete restrict on update restrict;

alter table dlog_j_reply add constraint fk_r_user_j_reply foreign key (userid)
      references dlog_user (userid) on delete restrict on update restrict;

alter table dlog_link add constraint fk_r_site_link foreign key (site_id)
      references dlog_site (site_id) on delete restrict on update restrict;

alter table dlog_message add constraint fk_r_msg_receiver foreign key (userid)
      references dlog_user (userid) on delete restrict on update restrict;

alter table dlog_music add constraint fk_r_music_box foreign key (music_box_id)
      references dlog_musicbox (music_box_id) on delete restrict on update restrict;

alter table dlog_music add constraint fk_r_recommend foreign key (userid)
      references dlog_user (userid) on delete restrict on update restrict;

alter table dlog_music add constraint fk_r_site_music foreign key (site_id)
      references dlog_site (site_id) on delete restrict on update restrict;

alter table dlog_musicbox add constraint fk_r_site_mbox foreign key (site_id)
      references dlog_site (site_id) on delete restrict on update restrict;

alter table dlog_p_reply add constraint fk_r_photo_reply foreign key (photo_id)
      references dlog_photo (photo_id) on delete restrict on update restrict;

alter table dlog_p_reply add constraint fk_r_site_p_reply foreign key (site_id)
      references dlog_site (site_id) on delete restrict on update restrict;

alter table dlog_p_reply add constraint fk_r_user_p_reply foreign key (userid)
      references dlog_user (userid) on delete restrict on update restrict;

alter table dlog_photo add constraint fk_r_album_photo foreign key (album_id)
      references dlog_album (album_id) on delete restrict on update restrict;

alter table dlog_photo add constraint fk_r_photo_owner foreign key (userid)
      references dlog_user (userid) on delete restrict on update restrict;

alter table dlog_photo add constraint fk_r_site_photo foreign key (site_id)
      references dlog_site (site_id) on delete restrict on update restrict;

alter table dlog_site add constraint fk_r_site_type foreign key (dlog_type_id)
      references dlog_type (dlog_type_id) on delete restrict on update restrict;

alter table dlog_site add constraint fk_r_user_site foreign key (userid)
      references dlog_user (userid) on delete restrict on update restrict;

alter table dlog_t_reply add constraint fk_r_site_t_reply foreign key (site_id)
      references dlog_site (site_id) on delete restrict on update restrict;

alter table dlog_t_reply add constraint fk_r_topic_reply foreign key (topic_id)
      references dlog_topic (topic_id) on delete restrict on update restrict;

alter table dlog_t_reply add constraint fk_r_user_t_reply foreign key (userid)
      references dlog_user (userid) on delete restrict on update restrict;

alter table dlog_tag add constraint fk_r_site_tag foreign key (site_id)
      references dlog_site (site_id) on delete restrict on update restrict;

alter table dlog_topic add constraint fk_r_forum_topic foreign key (forum_id)
      references dlog_forum (forum_id) on delete restrict on update restrict;

alter table dlog_topic add constraint fk_r_site_topic foreign key (site_id)
      references dlog_site (site_id) on delete restrict on update restrict;

alter table dlog_topic add constraint fk_r_user_topic foreign key (userid)
      references dlog_user (userid) on delete restrict on update restrict;

alter table dlog_type add constraint fk_r_site_type_tree foreign key (dlo_dlog_type_id)
      references dlog_type (dlog_type_id) on delete restrict on update restrict;

alter table dlog_user add constraint fk_r_site_user foreign key (site_id)
      references dlog_site (site_id) on delete restrict on update restrict;

/*====================================好友分组==================================*/

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

/*========================================发送表==================================*/
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

/*=========================================接收表======================================*/
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

/*===============================公告系统表===================================================*/
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

/*==============================================附件===============================================*/
create table dlog_diary_annex ( 
	an_id 				int 		not null auto_increment, 
	user_id				int 		not null,
	site_id				int 		not null,
	upload_time	  		datetime	not null,
	count				int			not null,
	status				int,
	validate			bigint		not null,
	type				varchar(32)	,
	url					varchar(256) not null,
	disk_path			varchar(256)	not null,
	file_name			varchar(64)	not null,
	file_desc			varchar(128),
	annex_name			varchar(64)	not null,
	diary_id			int,
	file_size			int			not null,
	last_download		datetime,
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

/*========================================板表====================================*/
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