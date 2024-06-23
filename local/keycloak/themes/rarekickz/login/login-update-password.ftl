<#import "template.ftl" as layout>
<#import "password-commons.ftl" as passwordCommons>
<@layout.registrationLayout displayMessage=!messagesPerField.existsError('password','password-confirm'); section>
    <#if section = "header">
        ${msg("updatePasswordTitle")}
    <#elseif section = "form">
        <form id="kc-passwd-update-form" class="${properties.kcFormClass!}" action="${url.loginAction}" method="post">
            <div class="form-floating">
                <input type="password" id="password-new" name="password-new" class="form-control"
                       autofocus autocomplete="new-password"
                       aria-invalid="<#if messagesPerField.existsError('password','password-confirm')>true</#if>"
                />
                <label for="password-new">${msg("passwordNew")}</label>
                <#--                        <button class="${properties.kcFormPasswordVisibilityButtonClass!}" type="button" aria-label="${msg('showPassword')}"-->
                <#--                                aria-controls="password-new"  data-password-toggle-->
                <#--                                data-icon-show="${properties.kcFormPasswordVisibilityIconShow!}" data-icon-hide="${properties.kcFormPasswordVisibilityIconHide!}"-->
                <#--                                data-label-show="${msg('showPassword')}" data-label-hide="${msg('hidePassword')}">-->
                <#--                            <i class="${properties.kcFormPasswordVisibilityIconShow!}" aria-hidden="true"></i>-->
                <#--                        </button>-->

                <#if messagesPerField.existsError('password')>
                    <span id="input-error-password" class="${properties.kcInputErrorMessageClass!}"
                          aria-live="polite">
                            ${kcSanitize(messagesPerField.get('password'))?no_esc}
                        </span>
                </#if>
            </div>

            <div class="form-floating">
                <input type="password" id="password-confirm" name="password-confirm"
                       class="form-control"
                       autocomplete="new-password"
                       aria-invalid="<#if messagesPerField.existsError('password-confirm')>true</#if>"
                />
                <label for="password-confirm">${msg("passwordConfirm")}</label>
                <#--                        <button class="${properties.kcFormPasswordVisibilityButtonClass!}" type="button"-->
                <#--                                aria-label="${msg('showPassword')}"-->
                <#--                                aria-controls="password-confirm" data-password-toggle-->
                <#--                                data-icon-show="${properties.kcFormPasswordVisibilityIconShow!}"-->
                <#--                                data-icon-hide="${properties.kcFormPasswordVisibilityIconHide!}"-->
                <#--                                data-label-show="${msg('showPassword')}" data-label-hide="${msg('hidePassword')}">-->
                <#--                            <i class="${properties.kcFormPasswordVisibilityIconShow!}" aria-hidden="true"></i>-->
                <#--                        </button>-->

                <#if messagesPerField.existsError('password-confirm')>
                    <span id="input-error-password-confirm" class="${properties.kcInputErrorMessageClass!}"
                          aria-live="polite">
                            ${kcSanitize(messagesPerField.get('password-confirm'))?no_esc}
                        </span>
                </#if>
            </div>

            <div class="${properties.kcFormGroupClass!}">
                <#--                <@passwordCommons.logoutOtherSessions/>-->

                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                    <#if isAppInitiatedAction??>
                        <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonLargeClass!}"
                               type="submit" value="${msg("doSubmit")}"/>
                        <button
                        class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!} ${properties.kcButtonLargeClass!}"
                        type="submit" name="cancel-aia" value="true" />${msg("doCancel")}</button>
                    <#else>
                        <input class="btn btn-outline-charcoal-alt w-100 ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}"
                               type="submit" value="${msg("doSubmit")}"/>
                    </#if>
                </div>
            </div>
        </form>
        <script type="module" src="${url.resourcesPath}/js/passwordVisibility.js"></script>
    </#if>
</@layout.registrationLayout>