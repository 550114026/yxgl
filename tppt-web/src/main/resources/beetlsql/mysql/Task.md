list
===
SELECT t.*,e.name eventname,u.`NAME` as username,
case when e.status=1 and now() BETWEEN e.votebegintime and e.voteendtime then '执行中' else '已失效' end as statusname
FROM `t_task` t 
left join t_event e on t.event_id=e.id
join tfw_user u on t.creatruserid=u.ID
where  t.status=1
@if (!isEmpty(keystr)) {
	and (e.id=#{keystr} or e.name like CONCAT('%',#{keystr},'%') or e.memo like CONCAT('%',#{keystr},'%') )
@}
@if (!isEmpty(startDate)) {
	and ( DATE_FORMAT(t..createtime,'%Y-%m-%d' )>=DATE_FORMAT(#{startDate},'%Y-%m-%d')  )
@}
@if (!isEmpty(endDate)) {
	and ( DATE_FORMAT(e.createtime,'%Y-%m-%d' )<=DATE_FORMAT(#{endDate},'%Y-%m-%d')  )
@}

todolist
===
select t.* 
from t_task t 
join t_event e on t.event_id=e.id
where t.status=1 
and e.status=1
and now() BETWEEN e.votebegintime and e.voteendtime
@if (!isEmpty(batchno)) {
	and ( execbatchno is null or execbatchno !=#{batchno} )
@}
limit 20;

enabletask
===
update t_task
set status=0 
where status=1
@if (!isEmpty(eids)) {
	and event_id in (#{eids})
@}
@if (!isEmpty(mids)) {
	and merchantid in (#{mids})
@}
