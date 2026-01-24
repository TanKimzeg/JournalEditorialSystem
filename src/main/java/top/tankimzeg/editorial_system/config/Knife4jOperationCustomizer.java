package top.tankimzeg.editorial_system.config;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.List;

/**
 * @author Kim
 * @date 2026/1/20 23:24
 * @description 为所有Knife4j界面调试接口添加Authorization请求头部
 * @see <a href="https://doc.xiaominfo.com/docs/blog/add-authorization-header">OpenAPI3规范中添加Authorization鉴权请求</a>
 */
@Component
public class Knife4jOperationCustomizer implements GlobalOperationCustomizer {
    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        List<SecurityRequirement> securityRequirements = operation.getSecurity();
        if (securityRequirements == null) {
            securityRequirements = List.of(new SecurityRequirement()
                    .addList(HttpHeaders.AUTHORIZATION));
            operation.setSecurity(securityRequirements);
        }
        return operation;
    }
}
