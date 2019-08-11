/**
 * Copyright (c) 2015-2016, Chill Zhuang 庄骞 (smallchill@163.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ikkong.core.base.service.impl;

import com.ikkong.core.aop.AopContext;
import com.ikkong.core.base.BaseController;
import com.ikkong.core.base.service.CurdService;
import com.ikkong.core.dao.Blade;
import com.ikkong.core.meta.MetaIntercept;
import com.ikkong.core.toolbox.Record;
import com.ikkong.core.toolbox.grid.GridManager;

public class CurdServiceImpl implements CurdService {

	public boolean save(BaseController ctrl, Object model, Class<?> modelClass,
			MetaIntercept intercept) {
		AopContext ac = null;
		if (null != intercept) {
			ac = new AopContext(ctrl, model);
			intercept.saveBefore(ac);
		}
		String rtid = Blade.create(modelClass).saveRtStrId(model);
		boolean tempAfter = true;
		if (null != intercept && rtid.length() > 0) {
			ac.setId(rtid);
			tempAfter = intercept.saveAfter(ac);
		}
		return (rtid.length() > 0 && tempAfter);
	}

	public boolean update(BaseController ctrl, Object model,
			Class<?> modelClass, MetaIntercept intercept) {
		AopContext ac = null;
		if (null != intercept) {
			ac = new AopContext(ctrl, model);
			intercept.updateBefore(ac);
		}
		boolean temp = Blade.create(modelClass).update(model);
		boolean tempAfter = true;
		if (null != intercept && temp) {
			tempAfter = intercept.updateAfter(ac);
		}
		return (temp && tempAfter);
	}

	public boolean deleteByIds(BaseController ctrl, String ids,
			Class<?> modelClass, MetaIntercept intercept) {
		AopContext ac = null;
		if (null != intercept) {
			ac = new AopContext(ctrl);
			ac.setId(ids);
			intercept.removeBefore(ac);
		}
		int n = Blade.create(modelClass).deleteByIds(ids);
		boolean tempAfter = true;
		if (null != intercept && n > 0) {
			tempAfter = intercept.removeAfter(ac);
		}
		return (n > 0 && tempAfter);
	}

	public boolean delByIds(BaseController ctrl, String ids,
			Class<?> modelClass, MetaIntercept intercept) {
		AopContext ac = null;
		if (null != intercept) {
			ac = new AopContext(ctrl);
			ac.setId(ids);
			intercept.delBefore(ac);
		}
		boolean temp = Blade.create(modelClass).updateBy("status = #{status}", "id in (#{join(ids)})", Record.create().set("status", 5).set("ids", ids.split(",")));
		boolean tempAfter = true;
		if (null != intercept && temp) {
			tempAfter = intercept.delAfter(ac);
		}
		return (temp && tempAfter);
	}

	public boolean restoreByIds(BaseController ctrl, String ids,
			Class<?> modelClass, MetaIntercept intercept) {
		AopContext ac = null;
		if (null != intercept) {
			ac = new AopContext(ctrl);
			ac.setId(ids);
			intercept.restoreBefore(ac);
		}
		boolean temp = Blade.create(modelClass).updateBy("status = #{status}", "id in (#{join(ids)})", Record.create().set("status", 1).set("ids", ids.split(",")));
		boolean tempAfter = true;
		if (null != intercept && temp) {
			tempAfter = intercept.restoreAfter(ac);
		}
		return (temp && tempAfter);
	}

	public Object paginate(Integer page, Integer rows, String source,
			String para, String sort, String order, MetaIntercept intercept,
			BaseController ctrl) {
		Object list = GridManager.paginate(page, rows, source, para, sort, order, intercept, ctrl);
		return list;
	}

}
