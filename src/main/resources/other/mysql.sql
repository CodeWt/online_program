#1.如果发现表中已经有此行数据（根据主键或者唯一索引判断）则先删除此行数据，然后插入新的数据。否则，直接插入新数据。
#2.插入数据的表必须有主键或者是唯一索引！否则的话，replace into 会直接插入数据，这将导致表中出现重复的数据。
replace into code (code_id, code_text, user_id)
values ("n0651f105681462fa6fb00279ca1c8d6","xxx",1);

insert into code(code_id, code_text, user_id)
values ("n0651f105681462fa6fb00279ca1c8d6",'xxx',1111)
on duplicate key update code_text='jjjj',update_time=now();

insert ignore into code(code_id, code_text, user_id)
VALUES ('n0651f105681462fa6fb00279ca1c8d6','ggggg',1111);

select * from code where code_id='n0651f105681462fa6fb00279ca1c8d6'

