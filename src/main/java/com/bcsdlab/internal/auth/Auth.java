package com.bcsdlab.internal.auth;

public @interface Auth {

    Authority[] permit();
}
