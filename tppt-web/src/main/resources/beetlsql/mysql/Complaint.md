list
===
select c.*,e.name as ename,m.name as mname,m.indexno,u.nickname, d.name cname
from t_complaint c
join t_event e on c.eventid=e.id
left join t_merchant m on c.merchantid=m.id
join t_vote_user u on c.openid=u.openid
left join tfw_dict d on d.`CODE`='10033' and c.ctype=d.num
order by c.id desc 