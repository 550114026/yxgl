list
===
select e.*,u.`NAME` as userName,u.ACCOUNT,u.PHONE,
(select count(0) from t_merchant m where m.eventid=e.id and m.source=1 and m.status=1) qd,
(select count(0) from t_merchant m where m.eventid=e.id and m.source=2 and m.status=1) zz,
(select sum(tickets) from  t_vote_list vl where vl.eventid=e.id and vl.status in(1,3)) totle,
(select sum(tickets) from  t_vote_list vl where vl.eventid=e.id and vl.status=3 and vl.votetype=2) viptotle,
(select sum(amount) from  t_vote_list vl where vl.eventid=e.id and vl.status=3 and vl.votetype=2) vipamount,
case when now()<e.votebegintime then '未开始'
		 when now() BETWEEN e.votebegintime and e.voteendtime then '进行中'
		 when now()> e.voteendtime then '已结束'
		 else '未知' end as statusName
from t_event e 
join tfw_user u on e.creatr_user_id=u.ID 
where status='1'
@if (!isEmpty(keystr)) {
	and (e.name like CONCAT('%',#{keystr},'%') or e.memo like CONCAT('%',#{keystr},'%') )
@}
@if (!isEmpty(status)&&status!="1") {
    @if (status=="2") {    
	   and (now() BETWEEN e.joinbegintime and e.joinendtime  or  now() BETWEEN e.votebegintime and e.voteendtime)
    @}else if(status=="3"){
        and (e.votebegintime >now())
    @}else if(status=="4"){
                and (e.voteendtime <now())
    @}else if(status=="5"){
                and  ( DATE_FORMAT(now(),'%Y-%m-%d')=DATE_FORMAT(e.votebegintime,'%Y-%m-%d' )  )
    @}else if(status=="6"){
                    and  (DATE_FORMAT(now(),'%Y-%m-%d')=DATE_FORMAT(e.voteendtime,'%Y-%m-%d' )   )
    @}
@}