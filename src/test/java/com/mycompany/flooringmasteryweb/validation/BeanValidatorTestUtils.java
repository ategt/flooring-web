package com.mycompany.flooringmasteryweb.validation;

import org.hibernate.validator.internal.engine.ValidationContext;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorManager;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ConstraintValidator;
import javax.xml.validation.Validator;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;

public class BeanValidatorTestUtils {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <A extends Annotation, E> void replaceValidatorInContext(Validator validator,
                                                                           final ConstraintValidator<A, ?> validatorInstance,
                                                                           E instanceToBeValidated) {
        final Class<A> anotacaoDoValidador = (Class<A>)
                ((ParameterizedType) validatorInstance.getClass().getGenericInterfaces()[0])
                        .getActualTypeArguments()[0];

        ValidationContext.ValidationContextBuilder valCtxBuilder = ReflectionTestUtils.<ValidationContext.ValidationContextBuilder>invokeMethod(validator,
                "getValidationContext");
        ValidationContext<E> validationContext = valCtxBuilder.forValidate(instanceToBeValidated);
        ConstraintValidatorManager constraintValidatorManager = validationContext.getConstraintValidatorManager();

        final ConcurrentHashMap nonSpyHashMap = new ConcurrentHashMap();
        ConcurrentHashMap spyHashMap = spy(nonSpyHashMap);
        doAnswer(new Answer<Object>() {
            @Override public Object answer(InvocationOnMock invocation) throws Throwable {
                Object key = invocation.getArguments()[0];
                Object keyAnnotation = ReflectionTestUtils.getField(key, "annotation");
                if (anotacaoDoValidador.isInstance(keyAnnotation)) {
                    return validatorInstance;
                }
                return nonSpyHashMap.get(key);
            }
        }).when(spyHashMap).get(any());

        ReflectionTestUtils.setField(constraintValidatorManager, "constraintValidatorCache", spyHashMap);
    }

}