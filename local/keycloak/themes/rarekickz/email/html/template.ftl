<#macro emailLayout>
    <html lang="en">
    <head>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65"
              crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&display=swap"
              rel="stylesheet">

        <style>
            body {
                font-family: "Montserrat", sans-serif;
            }

            .btn-outline-charcoal-alt {
                border-radius: 0;
                --bs-btn-color: #fff;
                background-color: #36454f;
                --bs-btn-border-color: transparent;
                --bs-btn-hover-color: #fff;
                --bs-btn-hover-bg: #585c5f;
                --bs-btn-hover-border-color: #585c5f;
                --bs-btn-focus-shadow-rgb: 33, 37, 41;
                --bs-btn-active-color: #fff;
                --bs-btn-active-bg: #36454f;
                --bs-btn-active-border-color: #36454f;
                --bs-btn-active-shadow: inset 0 3px 5px rgba(0, 0, 0, 0.125);
                --bs-btn-disabled-color: #36454f;
                --bs-btn-disabled-bg: transparent;
                --bs-btn-disabled-border-color: #36454f;
                --bs-gradient: none;
                font-weight: 600;
            }
        </style>
    </head>
    <body>
    <#nested>
    </body>
    </html>
</#macro>