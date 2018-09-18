package com.zhihu.demo.vo.validator;

import com.zhihu.demo.util.ValidatorUtil;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsIllegalValidator implements ConstraintValidator<IsIllegal, String> {
    /* *
     * ConstraintValidator<A,T> A: annotation T:Type of being annotated object
     */

    private boolean required = false;

    @Override
    public void initialize(IsIllegal constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (required){
            // if parameter is required
            return !ValidatorUtil.isIllegal(s);
        }else {
            // if parameter be null is permitted
            return StringUtils.isEmpty(s) || !ValidatorUtil.isIllegal(s);
        }
    }


}
