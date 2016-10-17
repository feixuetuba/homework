/*==============================================================*/
/* DBMS name:      Microsoft SQL Server 2000                    */
/* Created on:     2008-3-25 16:17:44                           */
/*==============================================================*/



/*==============================================================*/
/* Table: dlog_album                                            */
/*==============================================================*/
create table dlog_album (
   album_id             numeric              identity,
   photo_id             numeric              null,
   dlo_album_id         numeric              null,
   site_id              numeric              not null,
   dlog_type_id         numeric              null,
   album_name           varchar(40)          not null,
   album_desc           varchar(200)         null,
   photo_count          int                  not null,
   album_type           int                  not null,
   verifyCode           varchar(20)          null,
   sort_order           int                  not null,
   create_time          datetime             not null,
   constraint PK_DLOG_ALBUM primary key nonclustered (album_id)
)
go

/*==============================================================*/
/* Index: r_site_album_FK                                       */
/*==============================================================*/
create index r_site_album_FK on dlog_album (
site_id ASC
)
go

/*==============================================================*/
/* Index: r_album_FK                                            */
/*==============================================================*/
create index r_album_FK on dlog_album (
dlo_album_id ASC
)
go

/*==============================================================*/
/* Index: r_album_type_FK                                       */
/*==============================================================*/
create index r_album_type_FK on dlog_album (
dlog_type_id ASC
)
go

/*==============================================================*/
/* Index: r_album_cover_FK                                      */
/*==============================================================*/
create index r_album_cover_FK on dlog_album (
photo_id ASC
)
go

/*==============================================================*/
/* Index: album_id                                              */
/*==============================================================*/
create index album_id on dlog_album (
album_id ASC
)
go

/*==============================================================*/
/* Table: dlog_blocked_ip                                       */
/*==============================================================*/
create table dlog_blocked_ip (
   blocked_ip_id        numeric              identity,
   site_id              numeric              null,
   ip_addr              int                  not null,
   s_ip_addr            varchar(16)          not null,
   ip_mask              int                  not null,
   s_ip_mask            varchar(16)          not null,
   blocked_type         smallint             not null,
   blocked_time         datetime             not null,
   status               smallint             not null,
   constraint PK_DLOG_BLOCKED_IP primary key nonclustered (blocked_ip_id)
)
go

/*==============================================================*/
/* Index: r_site_blocked_ip_FK                                  */
/*==============================================================*/
create index r_site_blocked_ip_FK on dlog_blocked_ip (
site_id ASC
)
go

/*==============================================================*/
/* Index: blocked_ip_id                                         */
/*==============================================================*/
create index blocked_ip_id on dlog_blocked_ip (
blocked_ip_id ASC
)
go

/*==============================================================*/
/* Table: dlog_bookmark                                         */
/*==============================================================*/
create table dlog_bookmark (
   mark_id              numeric              identity,
   userid               numeric              not null,
   site_id              numeric              not null,
   parent_id            int                  not null,
   parent_type          smallint             not null,
   title                varchar(200)         null,
   url                  varchar(200)         null,
   create_time          datetime             not null,
   constraint PK_DLOG_BOOKMARK primary key nonclustered (mark_id)
)
go

/*==============================================================*/
/* Index: r_user_mark_FK                                        */
/*==============================================================*/
create index r_user_mark_FK on dlog_bookmark (
userid ASC
)
go

/*==============================================================*/
/* Index: r_site_bookmark_FK                                    */
/*==============================================================*/
create index r_site_bookmark_FK on dlog_bookmark (
site_id ASC
)
go

/*==============================================================*/
/* Index: mark_id                                               */
/*==============================================================*/
create index mark_id on dlog_bookmark (
mark_id ASC
)
go

/*==============================================================*/
/* Table: dlog_bulletin                                         */
/*==============================================================*/
create table dlog_bulletin (
   bulletin_id          numeric              identity,
   site_id              numeric              not null,
   bulletin_type        int                  not null,
   pub_time             datetime             not null,
   status               smallint             not null,
   title                varchar(200)         not null,
   content              text                 not null,
   constraint PK_DLOG_BULLETIN primary key nonclustered (bulletin_id)
)
go

/*==============================================================*/
/* Index: r_site_bulletin_FK                                    */
/*==============================================================*/
create index r_site_bulletin_FK on dlog_bulletin (
site_id ASC
)
go

/*==============================================================*/
/* Index: bulletin_id                                           */
/*==============================================================*/
create index bulletin_id on dlog_bulletin (
bulletin_id ASC
)
go

/*==============================================================*/
/* Table: dlog_catalog                                          */
/*==============================================================*/
create table dlog_catalog (
   catalog_id           numeric              identity,
   dlog_type_id         numeric              null,
   site_id              numeric              not null,
   catalog_name         varchar(20)          not null,
   catalog_desc         varchar(200)         null,
   create_time          datetime             not null,
   article_count        int                  not null,
   catalog_type         smallint             not null,
   verifyCode           varchar(20)          null,
   sort_order           int                  not null,
   constraint PK_DLOG_CATALOG primary key nonclustered (catalog_id)
)
go

/*==============================================================*/
/* Index: r_site_catalog_FK                                     */
/*==============================================================*/
create index r_site_catalog_FK on dlog_catalog (
site_id ASC
)
go

/*==============================================================*/
/* Index: r_catalog_type_FK                                     */
/*==============================================================*/
create index r_catalog_type_FK on dlog_catalog (
dlog_type_id ASC
)
go

/*==============================================================*/
/* Index: catalog_id                                            */
/*==============================================================*/
create index catalog_id on dlog_catalog (
catalog_id ASC
)
go

/*==============================================================*/
/* Table: dlog_catalog_perm                                     */
/*==============================================================*/
create table dlog_catalog_perm (
   catalog_id           numeric              not null,
   userid               numeric              not null,
   user_role            int                  not null,
   constraint PK_DLOG_CATALOG_PERM primary key (catalog_id, userid)
)
go

/*==============================================================*/
/* Index: dlog_catalog_perm_FK                                  */
/*==============================================================*/
create index dlog_catalog_perm_FK on dlog_catalog_perm (
catalog_id ASC
)
go

/*==============================================================*/
/* Index: dlog_catalog_perm2_FK                                 */
/*==============================================================*/
create index dlog_catalog_perm2_FK on dlog_catalog_perm (
userid ASC
)
go

/*==============================================================*/
/* Index: catalog_id                                            */
/*==============================================================*/
create index catalog_id on dlog_catalog_perm (
catalog_id ASC
)
go

/*==============================================================*/
/* Table: dlog_comments                                         */
/*==============================================================*/
create table dlog_comments (
   comment_id           numeric              identity,
   dlo_comment_id       numeric              null,
   owner_type           smallint             not null,
   owner_ident          int                  not null,
   entity_id            int                  not null,
   entity_type          int                  not null,
   client_ip            varchar(16)          not null,
   client_type          smallint             not null,
   client_user_agent    varchar(100)         null,
   author_id            int                  not null,
   author               varchar(20)          not null,
   author_email         varchar(50)          null,
   author_url           varchar(100)         null,
   title                varchar(200)         not null,
   content              text                 not null,
   content_format       smallint             not null,
   comment_time         datetime             not null,
   comment_flag         smallint             not null,
   comment_status       smallint             not null,
   constraint PK_DLOG_COMMENTS primary key nonclustered (comment_id)
)
go

/*==============================================================*/
/* Index: r_sub_comment_FK                                      */
/*==============================================================*/
create index r_sub_comment_FK on dlog_comments (
dlo_comment_id ASC
)
go

/*==============================================================*/
/* Index: comment_id                                            */
/*==============================================================*/
create index comment_id on dlog_comments (
comment_id ASC
)
go

/*==============================================================*/
/* Table: dlog_config                                           */
/*==============================================================*/
create table dlog_config (
   config_id            numeric              identity,
   site_id              numeric              null,
   config_name          varchar(20)          not null,
   int_value            int                  null,
   string_value         varchar(100)         null,
   date_value           datetime             null,
   time_value           datetime             null,
   timestamp_value      datetime             null,
   last_update          timestamp            not null,
   constraint PK_DLOG_CONFIG primary key nonclustered (config_id)
)
go

/*==============================================================*/
/* Index: r_site_config_FK                                      */
/*==============================================================*/
create index r_site_config_FK on dlog_config (
site_id ASC
)
go

/*==============================================================*/
/* Index: config_id                                             */
/*==============================================================*/
create index config_id on dlog_config (
config_id ASC
)
go

/*==============================================================*/
/* Table: dlog_diary                                            */
/*==============================================================*/
create table dlog_diary (
   diary_id             numeric              identity,
   userid               numeric              not null,
   site_id              numeric              not null,
   catalog_id           numeric              not null,
   author               varchar(20)          not null,
   author_url           varchar(100)         null,
   title                varchar(200)         not null,
   content              text                 not null,
   diary_size           int                  not null,
   refer                varchar(100)         null,
   weather              varchar(20)          not null,
   mood_level           smallint             not null,
   tags                 varchar(100)         null,
   bgsound              int                  null,
   reply_count          int                  not null,
   view_count           int                  not null,
   tb_count             int                  not null,
   client_type          smallint             not null,
   client_ip            varchar(16)          not null,
   client_user_agent    varchar(100)         null,
   write_time           datetime             not null,
   last_read_time       datetime             null,
   last_reply_time      datetime             null,
   modify_time          datetime             null,
   reply_notify         smallint             not null,
   diary_type           smallint             not null,
   locked               smallint             not null,
   status               smallint             not null,
   diary_annex          smallint             not null default 0,
   constraint PK_DLOG_DIARY primary key nonclustered (diary_id)
)
go

/*==============================================================*/
/* Index: r_site_journal_FK                                     */
/*==============================================================*/
create index r_site_journal_FK on dlog_diary (
site_id ASC
)
go

/*==============================================================*/
/* Index: r_catalog_diary_FK                                    */
/*==============================================================*/
create index r_catalog_diary_FK on dlog_diary (
catalog_id ASC
)
go

/*==============================================================*/
/* Index: r_user_journal_FK                                     */
/*==============================================================*/
create index r_user_journal_FK on dlog_diary (
userid ASC
)
go

/*==============================================================*/
/* Index: diary_id                                              */
/*==============================================================*/
create index diary_id on dlog_diary (
diary_id ASC
)
go

/*==============================================================*/
/* Table: dlog_diary_annex                                      */
/*==============================================================*/
create table dlog_diary_annex (
   annex_id             int                  identity,
   annex_user_id        int                  not null,
   annex_site_id        int                  not null,
   annex_upload_time    datetime             not null,
   annex_download_count int                  not null,
   annex_status         int                  null,
   annex_validate       bigint               not null,
   annex_type           int                  null,
   annex_url            varchar(256)         not null,
   annex_disk_path      varchar(256)         not null,
   annex_file_name      varchar(256)         not null,
   annex_file_desc      varchar(256)         null,
   annex_name           varchar(256)         not null,
   annex_diary_id       int                  null,
   annex_size           int                  not null,
   annex_last_time      datetime             null,
   annex_extend1        int                  null,
   annex_extend2        int                  null,
   annex_extend3        varchar(1024)        null,
   annex_extend4        varchar(1024)        null,
   constraint PK_DLOG_DIARY_ANNEX primary key nonclustered (annex_id)
)
go

/*==============================================================*/
/* Index: annex_id                                              */
/*==============================================================*/
create index annex_id on dlog_diary_annex (
annex_id ASC
)
go

/*==============================================================*/
/* Table: dlog_external_refer                                   */
/*==============================================================*/
create table dlog_external_refer (
   refer_id             numeric              identity,
   site_id              numeric              null,
   ref_id               int                  not null,
   ref_type             smallint             not null,
   refer_host           varchar(50)          null,
   refer_url            varchar(250)         not null,
   client_ip            varchar(16)          not null,
   refer_time           datetime             not null,
   constraint PK_DLOG_EXTERNAL_REFER primary key nonclustered (refer_id)
)
go

/*==============================================================*/
/* Index: r_site_refer_FK                                       */
/*==============================================================*/
create index r_site_refer_FK on dlog_external_refer (
site_id ASC
)
go

/*==============================================================*/
/* Index: refer_id                                              */
/*==============================================================*/
create index refer_id on dlog_external_refer (
refer_id ASC
)
go

/*==============================================================*/
/* Table: dlog_fck_upload_file                                  */
/*==============================================================*/
create table dlog_fck_upload_file (
   fck_file_id          numeric              identity,
   userid               numeric              not null,
   site_id              numeric              null,
   upload_time          datetime             not null,
   session_id           varchar(100)         not null,
   ref_id               int                  not null,
   ref_type             smallint             not null,
   save_path            varchar(255)         not null,
   file_uri             varchar(100)         not null,
   file_type            int                  not null,
   file_size            int                  not null,
   constraint PK_DLOG_FCK_UPLOAD_FILE primary key nonclustered (fck_file_id)
)
go

/*==============================================================*/
/* Index: r_site_file_FK                                        */
/*==============================================================*/
create index r_site_file_FK on dlog_fck_upload_file (
site_id ASC
)
go

/*==============================================================*/
/* Index: r_user_upload_FK                                      */
/*==============================================================*/
create index r_user_upload_FK on dlog_fck_upload_file (
userid ASC
)
go

/*==============================================================*/
/* Index: fck_file_id                                           */
/*==============================================================*/
create index fck_file_id on dlog_fck_upload_file (
fck_file_id ASC
)
go

/*==============================================================*/
/* Table: dlog_forum                                            */
/*==============================================================*/
create table dlog_forum (
   forum_id             numeric              identity,
   site_id              numeric              not null,
   dlog_type_id         numeric              null,
   forum_name           varchar(40)          not null,
   forum_desc           varchar(200)         null,
   forum_type           int                  not null,
   create_time          datetime             not null,
   modify_time          datetime             null,
   last_time            datetime             null,
   last_user_id         int                  null,
   last_user_name       varchar(50)          null,
   last_topic_id        int                  null,
   sort_order           int                  not null,
   topic_count          int                  not null,
   forum_option         int                  not null,
   status               smallint             not null,
   constraint PK_DLOG_FORUM primary key nonclustered (forum_id)
)
go

/*==============================================================*/
/* Index: r_site_forum_FK                                       */
/*==============================================================*/
create index r_site_forum_FK on dlog_forum (
site_id ASC
)
go

/*==============================================================*/
/* Index: r_forum_type_FK                                       */
/*==============================================================*/
create index r_forum_type_FK on dlog_forum (
dlog_type_id ASC
)
go

/*==============================================================*/
/* Index: forum_id                                              */
/*==============================================================*/
create index forum_id on dlog_forum (
forum_id ASC
)
go

/*==============================================================*/
/* Table: dlog_friend                                           */
/*==============================================================*/
create table dlog_friend (
   user_id              int                  not null,
   friend_id            int                  not null,
   friend_type          int                  not null,
   friend_role          int                  not null,
   add_time             datetime             not null,
   friend_group_id      int                  not null,
   constraint PK_DLOG_FRIEND primary key nonclustered (user_id, friend_id)
)
go

/*==============================================================*/
/* Index: user_id                                               */
/*==============================================================*/
create index user_id on dlog_friend (
user_id ASC
)
go

/*==============================================================*/
/* Table: dlog_guestbook                                        */
/*==============================================================*/
create table dlog_guestbook (
   guest_book_id        numeric              identity,
   userid               numeric              not null,
   site_id              numeric              not null,
   content              text                 not null,
   client_type          smallint             not null,
   client_ip            varchar(16)          not null,
   client_user_agent    varchar(100)         null,
   create_time          datetime             not null,
   reply_content        text                 null,
   reply_time           datetime             null,
   constraint PK_DLOG_GUESTBOOK primary key nonclustered (guest_book_id)
)
go

/*==============================================================*/
/* Index: r_site_guest_FK                                       */
/*==============================================================*/
create index r_site_guest_FK on dlog_guestbook (
site_id ASC
)
go

/*==============================================================*/
/* Index: r_user_liuyan_FK                                      */
/*==============================================================*/
create index r_user_liuyan_FK on dlog_guestbook (
userid ASC
)
go

/*==============================================================*/
/* Index: guest_book_id                                         */
/*==============================================================*/
create index guest_book_id on dlog_guestbook (
guest_book_id ASC
)
go

/*==============================================================*/
/* Table: dlog_j_reply                                          */
/*==============================================================*/
create table dlog_j_reply (
   j_reply_id           numeric              identity,
   site_id              numeric              not null,
   userid               numeric              null,
   diary_id             numeric              not null,
   author               varchar(20)          not null,
   author_url           varchar(100)         null,
   author_email         varchar(50)          null,
   client_type          smallint             not null,
   client_ip            varchar(16)          not null,
   client_user_agent    varchar(100)         null,
   owner_only           int                  not null,
   content              text                 not null,
   write_time           datetime             not null,
   status               smallint             not null,
   constraint PK_DLOG_J_REPLY primary key nonclustered (j_reply_id)
)
go

/*==============================================================*/
/* Index: r_site_j_reply_FK                                     */
/*==============================================================*/
create index r_site_j_reply_FK on dlog_j_reply (
site_id ASC
)
go

/*==============================================================*/
/* Index: r_journal_reply_FK                                    */
/*==============================================================*/
create index r_journal_reply_FK on dlog_j_reply (
diary_id ASC
)
go

/*==============================================================*/
/* Index: r_user_j_reply_FK                                     */
/*==============================================================*/
create index r_user_j_reply_FK on dlog_j_reply (
userid ASC
)
go

/*==============================================================*/
/* Index: j_reply_id                                            */
/*==============================================================*/
create index j_reply_id on dlog_j_reply (
j_reply_id ASC
)
go

/*==============================================================*/
/* Table: dlog_link                                             */
/*==============================================================*/
create table dlog_link (
   linkid               numeric              identity,
   site_id              numeric              not null,
   link_title           varchar(40)          not null,
   link_url             varchar(200)         not null,
   link_type            smallint             not null,
   sort_order           int                  not null,
   create_time          datetime             not null,
   status               smallint             not null,
   constraint PK_DLOG_LINK primary key nonclustered (linkid)
)
go

/*==============================================================*/
/* Index: r_site_link_FK                                        */
/*==============================================================*/
create index r_site_link_FK on dlog_link (
site_id ASC
)
go

/*==============================================================*/
/* Index: linkid                                                */
/*==============================================================*/
create index linkid on dlog_link (
linkid ASC
)
go

/*==============================================================*/
/* Table: dlog_message                                          */
/*==============================================================*/
create table dlog_message (
   msgid                numeric              identity,
   userid               numeric              null,
   from_user_id         int                  not null,
   from_user_name       varchar(50)          not null,
   content              text                 not null,
   send_time            datetime             not null,
   expire_time          datetime             null,
   read_time            datetime             null,
   status               smallint             not null,
   constraint PK_DLOG_MESSAGE primary key nonclustered (msgid)
)
go

/*==============================================================*/
/* Index: r_msg_receiver_FK                                     */
/*==============================================================*/
create index r_msg_receiver_FK on dlog_message (
userid ASC
)
go

/*==============================================================*/
/* Index: msgid                                                 */
/*==============================================================*/
create index msgid on dlog_message (
msgid ASC
)
go

/*==============================================================*/
/* Table: dlog_message_affiche                                  */
/*==============================================================*/
create table dlog_message_affiche (
   affiche_id           int                  identity,
   affiche_title        varchar(100)         not null,
   affiche_content      text                 not null,
   affiche_send_time    datetime             not null,
   affiche_status       int                  not null,
   affiche_user_id      int                  null,
   constraint PK_DLOG_MESSAGE_AFFICHE primary key nonclustered (affiche_id)
)
go

/*==============================================================*/
/* Index: affiche_id                                            */
/*==============================================================*/
create index affiche_id on dlog_message_affiche (
affiche_id ASC
)
go

/*==============================================================*/
/* Table: dlog_message_receiver                                 */
/*==============================================================*/
create table dlog_message_receiver (
   receiver_id          int                  identity,
   receiver_msg_id      int                  not null,
   receiver_user_id     int                  not null,
   receiver_s_user_id   int                  not null,
   receiver_s_user_name varchar(100)         not null,
   receiver_write_time  datetime             not null,
   receiver_status      int                  not null,
   constraint PK_DLOG_MESSAGE_RECEIVER primary key nonclustered (receiver_id)
)
go

/*==============================================================*/
/* Index: receiver_id                                           */
/*==============================================================*/
create index receiver_id on dlog_message_receiver (
receiver_id ASC
)
go

/*==============================================================*/
/* Table: dlog_message_send                                     */
/*==============================================================*/
create table dlog_message_send (
   send_id              int                  identity,
   send_msg_id          int                  not null,
   send_user_id         int                  not null,
   send_r_user_id       int                  not null,
   send_r_user_name     varchar(100)         not null,
   send_time            datetime             not null,
   send_status          int                  not null,
   constraint PK_DLOG_MESSAGE_SEND primary key nonclustered (send_id)
)
go

/*==============================================================*/
/* Index: send_id                                               */
/*==============================================================*/
create index send_id on dlog_message_send (
send_id ASC
)
go

/*==============================================================*/
/* Table: dlog_music                                            */
/*==============================================================*/
create table dlog_music (
   music_id             numeric              identity,
   music_box_id         numeric              null,
   userid               numeric              null,
   site_id              numeric              not null,
   music_title          varchar(100)         not null,
   music_word           text                 null,
   album                varchar(100)         null,
   singer               varchar(50)          null,
   url                  varchar(200)         null,
   create_time          datetime             not null,
   view_count           int                  not null,
   music_type           int                  not null,
   status               smallint             not null,
   constraint PK_DLOG_MUSIC primary key nonclustered (music_id)
)
go

/*==============================================================*/
/* Index: r_site_music_FK                                       */
/*==============================================================*/
create index r_site_music_FK on dlog_music (
site_id ASC
)
go

/*==============================================================*/
/* Index: r_music_box_FK                                        */
/*==============================================================*/
create index r_music_box_FK on dlog_music (
music_box_id ASC
)
go

/*==============================================================*/
/* Index: r_recommend_FK                                        */
/*==============================================================*/
create index r_recommend_FK on dlog_music (
userid ASC
)
go

/*==============================================================*/
/* Index: music_id                                              */
/*==============================================================*/
create index music_id on dlog_music (
music_id ASC
)
go

/*==============================================================*/
/* Table: dlog_musicbox                                         */
/*==============================================================*/
create table dlog_musicbox (
   music_box_id         numeric              identity,
   site_id              numeric              not null,
   box_name             varchar(40)          not null,
   box_desc             varchar(100)         null,
   music_count          int                  not null,
   create_time          datetime             not null,
   sort_order           int                  not null,
   constraint PK_DLOG_MUSICBOX primary key nonclustered (music_box_id)
)
go

/*==============================================================*/
/* Index: r_site_mbox_FK                                        */
/*==============================================================*/
create index r_site_mbox_FK on dlog_musicbox (
site_id ASC
)
go

/*==============================================================*/
/* Index: music_box_id                                          */
/*==============================================================*/
create index music_box_id on dlog_musicbox (
music_box_id ASC
)
go

/*==============================================================*/
/* Table: dlog_my_blacklist                                     */
/*==============================================================*/
create table dlog_my_blacklist (
   my_user_id           int                  not null,
   other_user_id        int                  not null,
   bl_type              int                  not null,
   add_time             datetime             not null,
   constraint PK_DLOG_MY_BLACKLIST primary key nonclustered (my_user_id, other_user_id)
)
go

/*==============================================================*/
/* Index: my_user_id                                            */
/*==============================================================*/
create index my_user_id on dlog_my_blacklist (
my_user_id ASC
)
go

/*==============================================================*/
/* Table: dlog_p_reply                                          */
/*==============================================================*/
create table dlog_p_reply (
   p_reply_id           numeric              identity,
   site_id              numeric              not null,
   photo_id             numeric              not null,
   userid               numeric              null,
   author               varchar(20)          not null,
   author_url           varchar(100)         null,
   author_email         varchar(50)          null,
   client_type          smallint             not null,
   client_ip            varchar(16)          not null,
   client_user_agent    varchar(100)         null,
   owner_only           int                  not null,
   content              text                 not null,
   write_time           datetime             not null,
   status               smallint             not null,
   constraint PK_DLOG_P_REPLY primary key nonclustered (p_reply_id)
)
go

/*==============================================================*/
/* Index: r_photo_reply_FK                                      */
/*==============================================================*/
create index r_photo_reply_FK on dlog_p_reply (
photo_id ASC
)
go

/*==============================================================*/
/* Index: r_site_p_reply_FK                                     */
/*==============================================================*/
create index r_site_p_reply_FK on dlog_p_reply (
site_id ASC
)
go

/*==============================================================*/
/* Index: r_user_p_reply_FK                                     */
/*==============================================================*/
create index r_user_p_reply_FK on dlog_p_reply (
userid ASC
)
go

/*==============================================================*/
/* Index: p_reply_id                                            */
/*==============================================================*/
create index p_reply_id on dlog_p_reply (
p_reply_id ASC
)
go

/*==============================================================*/
/* Table: dlog_photo                                            */
/*==============================================================*/
create table dlog_photo (
   photo_id             numeric              identity,
   site_id              numeric              not null,
   album_id             numeric              not null,
   userid               numeric              not null,
   photo_name           varchar(40)          not null,
   photo_desc           text                 null,
   file_name            varchar(100)         not null,
   photo_url            varchar(100)         not null,
   preview_url          varchar(100)         not null,
   tags                 varchar(100)         null,
   p_year               int                  not null,
   p_month              smallint             not null,
   p_date               smallint             not null,
   width                int                  not null,
   height               int                  not null,
   photo_size           bigint               not null,
   color_bit            int                  not null,
   exif_manufacturer    varchar(50)          null,
   exif_model           varchar(50)          null,
   exif_iso             int                  not null,
   exif_aperture        varchar(20)          null,
   exif_shutter         varchar(20)          null,
   exif_exposure_bias   varchar(20)          null,
   exif_exposure_time   varchar(20)          null,
   exif_focal_length    varchar(20)          null,
   exif_color_space     varchar(20)          null,
   reply_count          int                  not null,
   view_count           int                  not null,
   create_time          datetime             not null,
   modify_time          datetime             null,
   last_reply_time      datetime             null,
   photo_type           int                  not null,
   locked               smallint             not null,
   photo_status         smallint             not null,
   constraint PK_DLOG_PHOTO primary key nonclustered (photo_id)
)
go

/*==============================================================*/
/* Index: r_album_photo_FK                                      */
/*==============================================================*/
create index r_album_photo_FK on dlog_photo (
album_id ASC
)
go

/*==============================================================*/
/* Index: r_site_photo_FK                                       */
/*==============================================================*/
create index r_site_photo_FK on dlog_photo (
site_id ASC
)
go

/*==============================================================*/
/* Index: r_photo_owner_FK                                      */
/*==============================================================*/
create index r_photo_owner_FK on dlog_photo (
userid ASC
)
go

/*==============================================================*/
/* Index: photo_id                                              */
/*==============================================================*/
create index photo_id on dlog_photo (
photo_id ASC
)
go

/*==============================================================*/
/* Table: dlog_site                                             */
/*==============================================================*/
create table dlog_site (
   site_id              numeric              identity,
   userid               numeric              not null,
   dlog_type_id         numeric              null,
   site_name            varchar(20)          not null,
   site_c_name          varchar(50)          not null,
   site_url             varchar(100)         null,
   site_title           varchar(100)         null,
   site_detail          varchar(250)         null,
   site_icp             varchar(20)          null,
   site_logo            varchar(50)          null,
   site_css             varchar(50)          null,
   site_layout          varchar(50)          null,
   site_lang            varchar(10)          null,
   site_flag            int                  not null,
   create_time          datetime             not null,
   last_time            datetime             null,
   expired_time         datetime             null,
   last_exp_time        datetime             null,
   access_mode          smallint             not null,
   access_code          varchar(50)          null,
   diary_status         smallint             not null,
   photo_status         smallint             not null,
   music_status         smallint             not null,
   forum_status         smallint             not null,
   guestbook_status     smallint             not null,
   diary_cname          varchar(16)          null,
   photo_cname          varchar(16)          null,
   music_cname          varchar(16)          null,
   bbs_cname            varchar(16)          null,
   guestbook_cname      varchar(16)          null,
   photo_space_total    int                  not null,
   photo_space_used     int                  not null,
   diary_space_total    int                  not null,
   diary_space_used     int                  not null,
   media_space_total    int                  not null,
   media_space_used     int                  not null,
   site_type            int                  not null,
   site_level           int                  not null,
   status               smallint             not null,
   friend_cname         varchar(16)          null,
   friend_status        smallint             null,
   site_ext1            int                  not null default 0,
   site_ext2            int                  not null default 0,
   site_ext3            varchar(16)          null,
   site_ext4            varchar(16)          null,
   constraint PK_DLOG_SITE primary key nonclustered (site_id)
)
go

/*==============================================================*/
/* Index: r_user_site_FK                                        */
/*==============================================================*/
create index r_user_site_FK on dlog_site (
userid ASC
)
go

/*==============================================================*/
/* Index: r_site_type_FK                                        */
/*==============================================================*/
create index r_site_type_FK on dlog_site (
dlog_type_id ASC
)
go

/*==============================================================*/
/* Index: site_id                                               */
/*==============================================================*/
create index site_id on dlog_site (
site_id ASC
)
go

/*==============================================================*/
/* Table: dlog_site_stat                                        */
/*==============================================================*/
create table dlog_site_stat (
   site_stat_id         numeric              identity,
   site_id              numeric              null,
   stat_date            int                  not null,
   uv_count             int                  not null,
   pv_count             int                  not null,
   v_source             int                  not null,
   update_time          datetime             not null,
   constraint PK_DLOG_SITE_STAT primary key nonclustered (site_stat_id)
)
go

/*==============================================================*/
/* Index: r_site_stat_FK                                        */
/*==============================================================*/
create index r_site_stat_FK on dlog_site_stat (
site_id ASC
)
go

/*==============================================================*/
/* Index: site_stat_id                                          */
/*==============================================================*/
create index site_stat_id on dlog_site_stat (
site_stat_id ASC
)
go

/*==============================================================*/
/* Table: dlog_style                                            */
/*==============================================================*/
create table dlog_style (
   style_id             int                  identity,
   style_path_name      varchar(200)         not null,
   style_name           varchar(200)         not null,
   style_css            varchar(200)         not null,
   style_preview_l_image varchar(200)         not null,
   style_preview_s_image varchar(200)         not null,
   style_explain        varchar(1024)        null,
   style_status         int                  not null,
   style_type           int                  not null,
   style_level          int                  not null,
   style_count          int                  not null,
   style_create_time    datetime             null,
   style_extend1        int                  null,
   style_extend2        int                  null,
   style_extend3        varchar(1024)        null,
   style_extend4        varchar(1024)        null,
   constraint PK_DLOG_STYLE primary key nonclustered (style_id)
)
go

/*==============================================================*/
/* Index: style_id                                              */
/*==============================================================*/
create index style_id on dlog_style (
style_id ASC
)
go

/*==============================================================*/
/* Table: dlog_t_reply                                          */
/*==============================================================*/
create table dlog_t_reply (
   t_reply_id           numeric              identity,
   topic_id             numeric              not null,
   userid               numeric              not null,
   site_id              numeric              not null,
   title                varchar(200)         not null,
   content              text                 not null,
   write_time           datetime             null,
   status               smallint             null,
   client_ip            varchar(16)          null,
   client_type          smallint             null,
   client_user_agent    varchar(100)         null,
   constraint PK_DLOG_T_REPLY primary key nonclustered (t_reply_id)
)
go

/*==============================================================*/
/* Index: r_site_t_reply_FK                                     */
/*==============================================================*/
create index r_site_t_reply_FK on dlog_t_reply (
site_id ASC
)
go

/*==============================================================*/
/* Index: r_topic_reply_FK                                      */
/*==============================================================*/
create index r_topic_reply_FK on dlog_t_reply (
topic_id ASC
)
go

/*==============================================================*/
/* Index: r_user_t_reply_FK                                     */
/*==============================================================*/
create index r_user_t_reply_FK on dlog_t_reply (
userid ASC
)
go

/*==============================================================*/
/* Index: t_reply_id                                            */
/*==============================================================*/
create index t_reply_id on dlog_t_reply (
t_reply_id ASC
)
go

/*==============================================================*/
/* Table: dlog_tag                                              */
/*==============================================================*/
create table dlog_tag (
   tag_id               numeric              identity,
   site_id              numeric              not null,
   ref_id               int                  not null,
   ref_type             smallint             not null,
   tag_name             varchar(20)          not null,
   ref_time             int                  not null,
   constraint PK_DLOG_TAG primary key nonclustered (tag_id)
)
go

/*==============================================================*/
/* Index: r_site_tag_FK                                         */
/*==============================================================*/
create index r_site_tag_FK on dlog_tag (
site_id ASC
)
go

/*==============================================================*/
/* Index: tag_id                                                */
/*==============================================================*/
create index tag_id on dlog_tag (
tag_id ASC
)
go

/*==============================================================*/
/* Table: dlog_topic                                            */
/*==============================================================*/
create table dlog_topic (
   topic_id             numeric              identity,
   site_id              numeric              not null,
   userid               numeric              not null,
   forum_id             numeric              not null,
   username             varchar(40)          not null,
   create_time          datetime             not null,
   modify_time          datetime             null,
   title                varchar(200)         not null,
   content              text                 not null,
   tags                 varchar(100)         null,
   last_reply_time      datetime             null,
   last_reply_id        int                  null,
   last_user_id         int                  null,
   last_user_name       varchar(50)          null,
   reply_count          int                  not null,
   view_count           int                  not null,
   locked               smallint             not null,
   topic_type           int                  not null,
   status               smallint             not null,
   client_type          smallint             not null,
   client_ip            varchar(16)          not null,
   client_user_agent    varchar(100)         null,
   constraint PK_DLOG_TOPIC primary key nonclustered (topic_id)
)
go

/*==============================================================*/
/* Index: r_site_topic_FK                                       */
/*==============================================================*/
create index r_site_topic_FK on dlog_topic (
site_id ASC
)
go

/*==============================================================*/
/* Index: r_forum_topic_FK                                      */
/*==============================================================*/
create index r_forum_topic_FK on dlog_topic (
forum_id ASC
)
go

/*==============================================================*/
/* Index: r_user_topic_FK                                       */
/*==============================================================*/
create index r_user_topic_FK on dlog_topic (
userid ASC
)
go

/*==============================================================*/
/* Index: topic_id                                              */
/*==============================================================*/
create index topic_id on dlog_topic (
topic_id ASC
)
go

/*==============================================================*/
/* Table: dlog_trackback                                        */
/*==============================================================*/
create table dlog_trackback (
   track_id             numeric              identity,
   parent_id            int                  not null,
   parent_type          smallint             not null,
   refurl               varchar(100)         not null,
   title                varchar(200)         null,
   excerpt              varchar(200)         null,
   blog_name            varchar(50)          not null,
   remote_addr          char(15)             not null,
   track_time           datetime             not null,
   constraint PK_DLOG_TRACKBACK primary key nonclustered (track_id)
)
go

/*==============================================================*/
/* Index: track_id                                              */
/*==============================================================*/
create index track_id on dlog_trackback (
track_id ASC
)
go

/*==============================================================*/
/* Table: dlog_type                                             */
/*==============================================================*/
create table dlog_type (
   dlog_type_id         numeric              identity,
   dlo_dlog_type_id     numeric              null,
   type_name            varchar(20)          not null,
   sort_order           int                  not null,
   constraint PK_DLOG_TYPE primary key nonclustered (dlog_type_id)
)
go

/*==============================================================*/
/* Index: r_site_type_tree_FK                                   */
/*==============================================================*/
create index r_site_type_tree_FK on dlog_type (
dlo_dlog_type_id ASC
)
go

/*==============================================================*/
/* Index: dlog_type_id                                          */
/*==============================================================*/
create index dlog_type_id on dlog_type (
dlog_type_id ASC
)
go

/*==============================================================*/
/* Table: dlog_user                                             */
/*==============================================================*/
create table dlog_user (
   userid               numeric              identity,
   site_id              numeric              null,
   own_site_id          int                  not null,
   username             varchar(40)          not null,
   password             varchar(50)          not null,
   user_role            int                  not null,
   nickname             varchar(50)          not null,
   sex                  smallint             not null,
   birth                datetime             null,
   email                varchar(50)          null,
   homepage             varchar(50)          null,
   qq                   varchar(16)          null,
   msn                  varchar(50)          null,
   mobile               varchar(16)          null,
   nation               varchar(40)          null,
   province             varchar(40)          null,
   city                 varchar(40)          null,
   industry             varchar(40)          null,
   company              varchar(80)          null,
   address              varchar(200)         null,
   job                  varchar(40)          null,
   fax                  varchar(20)          null,
   tel                  varchar(20)          null,
   zip                  varchar(12)          null,
   portrait             varchar(100)         null,
   resume               varchar(200)         null,
   regtime              datetime             not null,
   last_time            datetime             null,
   last_ip              varchar(16)          null,
   keep_days            int                  not null,
   online_status        int                  not null,
   user_level           int                  not null,
   status               smallint             not null,
   article_count        int                  not null,
   article_reply_count  int                  not null,
   topic_count          int                  not null,
   topic_reply_count    int                  not null,
   photo_count          int                  not null,
   photo_reply_count    int                  not null,
   guestbook_count      int                  not null,
   bookmark_count       int                  not null,
   portrait_icon        char(100)            null,
   popedom              smallint             not null default 0,
   visitor              char(200)            null,
   user_ext1            int                  not null default 0,
   user_ext2            int                  not null default 0,
   user_ext3            varchar(16)          null,
   user_ext4            varchar(16)          null,
   constraint PK_DLOG_USER primary key nonclustered (userid)
)
go

/*==============================================================*/
/* Index: r_site_user_FK                                        */
/*==============================================================*/
create index r_site_user_FK on dlog_user (
site_id ASC
)
go

/*==============================================================*/
/* Index: userid                                                */
/*==============================================================*/
create index userid on dlog_user (
userid ASC
)
go

/*==============================================================*/
/* Table: dlog_user_f_group                                     */
/*==============================================================*/
create table dlog_user_f_group (
   group_id             int                  identity,
   group_user_id        int                  not null,
   group_name           varchar(64)          not null,
   group_type           int                  not null,
   group_count          int                  null,
   constraint PK_DLOG_USER_F_GROUP primary key nonclustered (group_id)
)
go

/*==============================================================*/
/* Index: group_id                                              */
/*==============================================================*/
create index group_id on dlog_user_f_group (
group_id ASC
)
go

alter table dlog_album
   add constraint FK_DLOG_ALB_R_ALBUM_DLOG_ALB foreign key (dlo_album_id)
      references dlog_album (album_id)
go

alter table dlog_album
   add constraint FK_DLOG_ALB_R_ALBUM_C_DLOG_PHO foreign key (photo_id)
      references dlog_photo (photo_id)
go

alter table dlog_album
   add constraint FK_DLOG_ALB_R_ALBUM_T_DLOG_TYP foreign key (dlog_type_id)
      references dlog_type (dlog_type_id)
go

alter table dlog_album
   add constraint FK_DLOG_ALB_R_SITE_AL_DLOG_SIT foreign key (site_id)
      references dlog_site (site_id)
go

alter table dlog_blocked_ip
   add constraint FK_DLOG_BLO_R_SITE_BL_DLOG_SIT foreign key (site_id)
      references dlog_site (site_id)
go

alter table dlog_bookmark
   add constraint FK_DLOG_BOO_R_SITE_BO_DLOG_SIT foreign key (site_id)
      references dlog_site (site_id)
go

alter table dlog_bookmark
   add constraint FK_DLOG_BOO_R_USER_MA_DLOG_USE foreign key (userid)
      references dlog_user (userid)
go

alter table dlog_bulletin
   add constraint FK_DLOG_BUL_R_SITE_BU_DLOG_SIT foreign key (site_id)
      references dlog_site (site_id)
go

alter table dlog_catalog
   add constraint FK_DLOG_CAT_R_CATALOG_DLOG_TYP foreign key (dlog_type_id)
      references dlog_type (dlog_type_id)
go

alter table dlog_catalog
   add constraint FK_DLOG_CAT_R_SITE_CA_DLOG_SIT foreign key (site_id)
      references dlog_site (site_id)
go

alter table dlog_catalog_perm
   add constraint FK_DLOG_CAT_DLOG_CATA_DLOG_CAT foreign key (catalog_id)
      references dlog_catalog (catalog_id)
go

alter table dlog_catalog_perm
   add constraint FK_DLOG_CAT_DLOG_CATA_DLOG_USE foreign key (userid)
      references dlog_user (userid)
go

alter table dlog_comments
   add constraint FK_DLOG_COM_R_SUB_COM_DLOG_COM foreign key (dlo_comment_id)
      references dlog_comments (comment_id)
go

alter table dlog_config
   add constraint FK_DLOG_CON_R_SITE_CO_DLOG_SIT foreign key (site_id)
      references dlog_site (site_id)
go

alter table dlog_diary
   add constraint FK_DLOG_DIA_R_CATALOG_DLOG_CAT foreign key (catalog_id)
      references dlog_catalog (catalog_id)
go

alter table dlog_diary
   add constraint FK_DLOG_DIA_R_SITE_JO_DLOG_SIT foreign key (site_id)
      references dlog_site (site_id)
go

alter table dlog_diary
   add constraint FK_DLOG_DIA_R_USER_JO_DLOG_USE foreign key (userid)
      references dlog_user (userid)
go

alter table dlog_external_refer
   add constraint FK_DLOG_EXT_R_SITE_RE_DLOG_SIT foreign key (site_id)
      references dlog_site (site_id)
go

alter table dlog_fck_upload_file
   add constraint FK_DLOG_FCK_R_SITE_FI_DLOG_SIT foreign key (site_id)
      references dlog_site (site_id)
go

alter table dlog_fck_upload_file
   add constraint FK_DLOG_FCK_R_USER_UP_DLOG_USE foreign key (userid)
      references dlog_user (userid)
go

alter table dlog_forum
   add constraint FK_DLOG_FOR_R_FORUM_T_DLOG_TYP foreign key (dlog_type_id)
      references dlog_type (dlog_type_id)
go

alter table dlog_forum
   add constraint FK_DLOG_FOR_R_SITE_FO_DLOG_SIT foreign key (site_id)
      references dlog_site (site_id)
go

alter table dlog_guestbook
   add constraint FK_DLOG_GUE_R_SITE_GU_DLOG_SIT foreign key (site_id)
      references dlog_site (site_id)
go

alter table dlog_guestbook
   add constraint FK_DLOG_GUE_R_USER_LI_DLOG_USE foreign key (userid)
      references dlog_user (userid)
go

alter table dlog_j_reply
   add constraint FK_DLOG_J_R_R_JOURNAL_DLOG_DIA foreign key (diary_id)
      references dlog_diary (diary_id)
go

alter table dlog_j_reply
   add constraint FK_DLOG_J_R_R_SITE_J__DLOG_SIT foreign key (site_id)
      references dlog_site (site_id)
go

alter table dlog_j_reply
   add constraint FK_DLOG_J_R_R_USER_J__DLOG_USE foreign key (userid)
      references dlog_user (userid)
go

alter table dlog_link
   add constraint FK_DLOG_LIN_R_SITE_LI_DLOG_SIT foreign key (site_id)
      references dlog_site (site_id)
go

alter table dlog_message
   add constraint FK_DLOG_MES_R_MSG_REC_DLOG_USE foreign key (userid)
      references dlog_user (userid)
go

alter table dlog_music
   add constraint FK_DLOG_MUS_R_MUSIC_B_DLOG_MUS foreign key (music_box_id)
      references dlog_musicbox (music_box_id)
go

alter table dlog_music
   add constraint FK_DLOG_MUS_R_RECOMME_DLOG_USE foreign key (userid)
      references dlog_user (userid)
go

alter table dlog_music
   add constraint FK_DLOG_MUS_R_SITE_MU_DLOG_SIT foreign key (site_id)
      references dlog_site (site_id)
go

alter table dlog_musicbox
   add constraint FK_DLOG_MUS_R_SITE_MB_DLOG_SIT foreign key (site_id)
      references dlog_site (site_id)
go

alter table dlog_p_reply
   add constraint FK_DLOG_P_R_R_PHOTO_R_DLOG_PHO foreign key (photo_id)
      references dlog_photo (photo_id)
go

alter table dlog_p_reply
   add constraint FK_DLOG_P_R_R_SITE_P__DLOG_SIT foreign key (site_id)
      references dlog_site (site_id)
go

alter table dlog_p_reply
   add constraint FK_DLOG_P_R_R_USER_P__DLOG_USE foreign key (userid)
      references dlog_user (userid)
go

alter table dlog_photo
   add constraint FK_DLOG_PHO_R_ALBUM_P_DLOG_ALB foreign key (album_id)
      references dlog_album (album_id)
go

alter table dlog_photo
   add constraint FK_DLOG_PHO_R_PHOTO_O_DLOG_USE foreign key (userid)
      references dlog_user (userid)
go

alter table dlog_photo
   add constraint FK_DLOG_PHO_R_SITE_PH_DLOG_SIT foreign key (site_id)
      references dlog_site (site_id)
go

alter table dlog_site
   add constraint FK_DLOG_SIT_R_SITE_TY_DLOG_TYP foreign key (dlog_type_id)
      references dlog_type (dlog_type_id)
go

alter table dlog_site
   add constraint FK_DLOG_SIT_R_USER_SI_DLOG_USE foreign key (userid)
      references dlog_user (userid)
go

alter table dlog_site_stat
   add constraint FK_DLOG_SIT_R_SITE_ST_DLOG_SIT foreign key (site_id)
      references dlog_site (site_id)
go

alter table dlog_t_reply
   add constraint FK_DLOG_T_R_R_SITE_T__DLOG_SIT foreign key (site_id)
      references dlog_site (site_id)
go

alter table dlog_t_reply
   add constraint FK_DLOG_T_R_R_TOPIC_R_DLOG_TOP foreign key (topic_id)
      references dlog_topic (topic_id)
go

alter table dlog_t_reply
   add constraint FK_DLOG_T_R_R_USER_T__DLOG_USE foreign key (userid)
      references dlog_user (userid)
go

alter table dlog_tag
   add constraint FK_DLOG_TAG_R_SITE_TA_DLOG_SIT foreign key (site_id)
      references dlog_site (site_id)
go

alter table dlog_topic
   add constraint FK_DLOG_TOP_R_FORUM_T_DLOG_FOR foreign key (forum_id)
      references dlog_forum (forum_id)
go

alter table dlog_topic
   add constraint FK_DLOG_TOP_R_SITE_TO_DLOG_SIT foreign key (site_id)
      references dlog_site (site_id)
go

alter table dlog_topic
   add constraint FK_DLOG_TOP_R_USER_TO_DLOG_USE foreign key (userid)
      references dlog_user (userid)
go

alter table dlog_type
   add constraint FK_DLOG_TYP_R_SITE_TY_DLOG_TYP foreign key (dlo_dlog_type_id)
      references dlog_type (dlog_type_id)
go

alter table dlog_user
   add constraint FK_DLOG_USE_R_SITE_US_DLOG_SIT foreign key (site_id)
      references dlog_site (site_id)
go

