<#import "template.ftl" as layout>

<@layout.emailLayout>
    <div class="text-center mt-4">
        <h3 class="text-uppercase fw-semibold">RareKickz</h3>
        <p>
            You are receiving this email because we received a request for resetting the password of your RareKickz
            account. <br/>
            Click the button below to reset your password, this link will expire within
            <span>${linkExpirationFormatter(linkExpiration)}</span>.
        </p>
        <a class="btn btn-outline-charcoal-alt my-4" href="${link}">Reset Password</a>
        <p class="small">If you don't want to reset your credentials, just ignore this message and nothing will be
            changed.</p>
    </div>
</@layout.emailLayout>