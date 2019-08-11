list
===
select vl.vote_time,vl.tickets,vl.amount,vl.price,vl.votetype,
case when vl.votetype=1 then '普通投票' else 'vip购票' end as voteTypeName,
case when vl.status='1' then '已生效' when vl.status='2' then '无效 未支付' else '已生效' end as statusName,
m.name as mName,e.name as eName,e.id as eid,m.indexno,
vu.ip,vu.province,vu.city,vu.nickname
from t_vote_list vl 
join t_event e on vl.eventid=e.id
join tfw_user u on e.creatr_user_id=u.ID 
join t_merchant m on vl.merchantid=m.id
join t_vote_user vu on vl.openid=vu.openid
where m.status=1 and e.status=1
and vl.vote_time>DATE_ADD(now(),INTERVAL -5 Day)
@if (!isEmpty(deptid)) {
    and u.DEPTID=#{deptid}
@}
@if (!isEmpty(eventid)) {
	and e.id=#{eventid}
@}
@if (!isEmpty(mid)) {
	and m.id=#{mid}
@}
@if (!isEmpty(votetype)) {
	and vl.votetype=#{votetype}
@}
@if (!isEmpty(status)) {
	and vl.status=#{status}
@}
@if (!isEmpty(keystr)) {
	and (vl.orderid=#{keystr} or e.id=#{keystr} or e.name like CONCAT('%',#{keystr},'%') or m.name like CONCAT('%',#{keystr},'%') or m.indexno=#{keystr})
@}
order by vl.vote_time desc


lastvipList
=== 
select vl.*,
m.name as mName,e.name as eName,e.id as eid,m.indexno,
vu.ip,vu.province,vu.city,vu.nickname
from t_vote_list vl 
join t_event e on vl.eventid=e.id
join tfw_user u on e.creatr_user_id=u.ID 
join t_merchant m on vl.merchantid=m.id
left join t_vote_user vu on vl.openid=vu.openid
where m.status=1 and e.status=1 and vl.status=3 and vl.votetype=2
@if (!isEmpty(deptid)) {
    and u.DEPTID=#{deptid}
@}
order by vl.vote_time desc
limit 20;
;

sumAmount
===
select sum(vl.amount) amount
from t_vote_list vl
join t_event e on vl.eventid=e.id
join tfw_user u on e.creatr_user_id=u.ID 
where vl.status=3 and vl.votetype=2 
@if (!isEmpty(deptid)) {
    and u.DEPTID=#{deptid}
@}
@if (!isEmpty(beginDate)) {
	and vl.vote_time>str_to_date(#{beginDate}, '%Y-%m-%d')
@}

@if (!isEmpty(endDate)) {
	and vl.vote_time<str_to_date(#{endDate}, '%Y-%m-%d')
@}


countInfo
===

select DATE_FORMAT(vote_time,'%Y-%m-%d') date,
sum( case when vl.status=3 and vl.votetype=2  then  vl.amount else 0 end) vipamount,
sum( case when vl.status=3 and vl.votetype=2  then  vl.tickets else 0 end) viptickets,
SUM(tickets) totletickets
from t_vote_list vl
join t_event e on vl.eventid=e.id
join tfw_user u on e.creatr_user_id=u.ID 
where vl.status in(1,3) 
@if (!isEmpty(deptid)) {
    and u.DEPTID=#{deptid}
@}
@if (!isEmpty(beginDate)) {
	and vl.vote_time>str_to_date(#{beginDate}, '%Y-%m-%d')
@}

@if (!isEmpty(endDate)) {
	and vl.vote_time<str_to_date(#{endDate}, '%Y-%m-%d')
@}
GROUP BY DATE_FORMAT(vote_time,'%Y-%m-%d')
order by DATE_FORMAT(vote_time,'%Y-%m-%d')


