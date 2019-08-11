list
===
select * from (
	select m.id, m.eventid, m.indexno, m.name, m.source, m.phone, m.lng, m.lat, m.pictures, 
	 m.summary, m.creatr_user_id, m.update_user_id, m.status, m.audit_status, m.create_time, 
	 m.update_time, m.version, m.homepage, m.totlevotes, ifnull(m.vipvotes,0) vipvotes, m.hits, m.openid,m.memo,
        e.name as eventName,
	ifnull(m.totlevotes,0)-ifnull(m.vipvotes,0) as ptp,
	ifnull(totleamount,0)as totleamount,ifnull(todayvipp,0)as todayvipp,ifnull(todayamount,0)as todayamount,ifnull(todaynomal,0)as todaynomal
    from t_merchant m 
    left join (
        select
        merchantid,
	    sum(case when vl.status=3 and vl.votetype=2 then amount else 0 end) as totleamount,
	    sum(case when vl.status=3 and vl.votetype=2 and vl.vote_time> curdate() then tickets else 0 end) as todayvipp,
	    sum(case when vl.status=3 and vl.votetype=2 and vl.vote_time> curdate() then amount else 0 end) as todayamount,
	    sum(case when  vl.vote_time> curdate() and vl.status=1 and vl.votetype=1 then tickets else 0 end)as todaynomal
        from t_vote_list vl  
        where 1=1 
        @if (!isEmpty(eventid)) {
            and vl.eventid=#{eventid}
        @}
        group by merchantid
    ) vl on m.id=vl.merchantid
	join t_event e on m.eventid=e.id
    join tfw_user u on e.creatr_user_id=u.ID 
    join tfw_dept d on u.DEPTID=d.ID
	where e.status=1 
    @if (!isEmpty(eventid)) {
        and e.id=#{eventid}
    @}
    @if (!isEmpty(userid)) {
        and e.creatr_user_id=#{userid}
    @}
    @if (!isEmpty(deptid)) {
        and d.ID=#{deptid}
    @}
) a 
where  1=1
@if (!isEmpty(status)) {
	and status=#{status}
@}else{
    and status=1
@}
@if (!isEmpty(eventid)) {
	and eventid=#{eventid}
@}
@if (!isEmpty(keystr)) {
	and ( indexno=#{keystr} or name like CONCAT('%',#{keystr},'%') or memo like CONCAT('%',#{keystr},'%') )
@}
@if (!isEmpty(audit_status)) {
	and audit_status=#{audit_status}
@}

@if (!isEmpty(sortfield)) {
	order by #{text(sortfield)} #{text(sortDirection)}
@}

excel
===
select indexno,name,phone 
from t_merchant m
where eventid=#{eventid} 
and status=1 
and audit_status=1
order by indexno

