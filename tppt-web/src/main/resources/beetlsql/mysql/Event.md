list
===
select e.id
from t_event e 
join tfw_user u on e.creatr_user_id=u.ID 
join tfw_dept d on u.DEPTID=d.ID
where 1=1
@if (!isEmpty(deptid)) {
	and d.ID=#{deptid}
@}

@if (!isEmpty(userid)) {
	and e.creatr_user_id=#{userid}
@}
@if (!isEmpty(delete_status)) {
	and e.`status`=#{delete_status}
@} else{
    and e.`status`='1'
@}
@if (!isEmpty(keystr)) {
	and ( e.id=#{keystr} or e.name like CONCAT('%',#{keystr},'%') or e.memo like CONCAT('%',#{keystr},'%') )
@}
@if (!isEmpty(status)&&status!="1") {
    @if (status=="2") {    
	   and (  now() BETWEEN e.votebegintime and e.voteendtime)
    @}else if(status=="3"){
        and (e.votebegintime >now())
    @}else if(status=="4"){
                and (e.voteendtime <now())
    @}else if(status=="5"){
                and  ( DATE_FORMAT(now(),'%Y-%m-%d')=DATE_FORMAT(e.votebegintime,'%Y-%m-%d' )  )
    @}else if(status=="6"){
                and  (DATE_FORMAT(now(),'%Y-%m-%d')=DATE_FORMAT(e.voteendtime,'%Y-%m-%d' )   )
    @}else if(status=="7"){
                and  (DATE_FORMAT(date_add(now(),INTERVAL -1 day),'%Y-%m-%d')=DATE_FORMAT(e.votebegintime,'%Y-%m-%d' )   )
     @}
@}
@if (!isEmpty(startDate)) {
	and ( DATE_FORMAT(e.voteendtime,'%Y-%m-%d' )>=DATE_FORMAT(#{startDate},'%Y-%m-%d')  )
@}
@if (!isEmpty(endDate)) {
	and ( DATE_FORMAT(e.voteendtime,'%Y-%m-%d' )<=DATE_FORMAT(#{endDate},'%Y-%m-%d')  )
@}

@if (!isEmpty(sortfield)) {
	order by #{text(sortfield)} #{text(sortDirection)}
@}

listShow
===
select e.id,e.name,e.channeltype,e.joinbegintime,e.joinendtime,e.votebegintime,e.voteendtime,e.tips,e.pictures,e.votelimit,e.vipenabled,
       e.vipprice,e.viplimit,e.banner,e.totlevotes,e.vipvotes,e.hits,e.creatr_user_id,e.creatr_user_name,e.update_user_id,e.status,
       e.create_time,e.update_time,e.version,e.memo,
u.`NAME` as userName,u.ACCOUNT,u.PHONE,
ifnull((select count(0) from t_merchant m where m.eventid=e.id and m.source=1 and m.status=1),0) qd,
ifnull((select count(0) from t_merchant m where m.eventid=e.id and m.source=2 and m.status=1),0) zz,
totlevotes totle,
ifnull(viptotle,0) viptotle,
ifnull(vipamount,0) vipamount,
ifnull(todayvipvote,0) todayvipvote,
ifnull(todayvote,0) todayvote,
case when now()<e.votebegintime then '未开始'
		 when now() BETWEEN e.votebegintime and e.voteendtime then '进行中'
		 when now()> e.voteendtime then '已结束'
		 else '未知' end as statusName,
(select id from t_task t where e.id=t.event_id and t.status=1  ) as taskid
from t_event e 
left join (
      select vl.eventid ,
	    sum(case when vl.status=3 and vl.votetype=2 then tickets else 0 end) as viptotle,
	    sum(case when vl.status=3 and vl.votetype=2 then amount else 0 end) as vipamount,
	    sum(case when vl.status=3 and vl.votetype=2 and vl.vote_time> curdate() then tickets else 0 end) as todayvipvote,
	    sum(case when  vl.status=1 and vl.votetype=1 and vl.vote_time> curdate() then tickets else 0 end) as todayvote
        from  t_vote_list vl  
        where vl.eventid  in(#{text(ids)})
       group by vl.eventid 
) vl on e.id=vl.eventid
join tfw_user u on e.creatr_user_id=u.ID 
join tfw_dept d on u.DEPTID=d.ID
where e.id in(#{text(ids)})
@if (!isEmpty(sortfield)) {
	order by #{text(sortfield)} #{text(sortDirection)}
@}



countevent
===
select count(0) 
from t_event e
join tfw_user u on e.creatr_user_id=u.ID 
join tfw_dept d on u.DEPTID=d.ID
where e.status=1
@if (!isEmpty(deptid)) {
	and d.ID=#{deptid}
@}
@if (!isEmpty(userid)) {
	and e.creatr_user_id=#{userid}
@}



eventstatistics
===
select d.FULLNAME,sum(case when e.id is null then 0 else 1 end ) as ecount,
sum(ifnull((select sum(ifnull(amount,0)) from  t_vote_list vl where vl.eventid=e.id and vl.status=3 and vl.votetype=2),0)) vipamount
from tfw_dept d 
left join (
select e.id,u.DEPTID
from t_event e 
join tfw_user u on e.creatr_user_id=u.ID 
where   1=1
@if (!isEmpty(startDate)) {
	and e.voteendtime>=#{startDate}
@}
@if (!isEmpty(endDate)) {
	and e.voteendtime<=#{endDate}
@}
@if (!isEmpty(deptid)) {
	and u.DEPTID=#{deptid}
@}
@if (!isEmpty(status)&&status!="1") {
    @if (status=="2") {    
	   and (  now() BETWEEN e.votebegintime and e.voteendtime)
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
) e on d.ID=e.DEPTID
where   d.isarea=2
group by d.FULLNAME



