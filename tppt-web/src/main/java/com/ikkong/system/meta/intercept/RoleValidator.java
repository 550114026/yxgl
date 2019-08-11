package com.ikkong.system.meta.intercept;

import com.ikkong.core.base.BaseValidator;
import com.jfinal.core.Controller;

/**
 * Created by ikkong on 2016/12/28.
 */
public class RoleValidator extends BaseValidator {
    @Override
    protected void validate(Controller c) {
        validateRequired("tfw_role.num","请输入排序号");
        //validateRequired("tfw_role.deptid","请选择角色部门");
    }

    @Override
    protected void handleError(Controller c) {
        c.renderJson(result);
    }
}
