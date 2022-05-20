# JWT demo

---

**Authorization vs Authentication**  
**Authentication** is a process of confirming that a user is **who** they say they 
are. User can authenticate by e.g. providing valid password for an account.  
**Authorization** on the other hand is a process verifying **what** a user can do 
and to what resources they have access to. It can be implemented as e.g. set of 
roles or/and permissions assigned to given account.

**JWT token structure**  
JWT token is made up of 3 parts:
* **Header** - contains information about the token, like signing algorithm and
token type
* **Payload** - actual data carried by the token (claims)
* **Signature** - token signature that can be used to verify token authenticity

JWT tokens **are not** encrypted by default (they are only Base64 encoded and 
then signed).

**How to make sure the JWT token is valid?**  
Always upon receiving JWT token:
* Verify its signature (with an algorithm from a whitelist)
* Check `iss` claim to ensure the token was issued by the party you expected to do it
* Check `aud` claim to ensure the token is meant for your application
* Check `exp`, `nbf` and `iat` claims to properly handle token expiration and other time rules
* Make sure you are not using Access Token JWT in place of ID token and vice versa

**Advantages of JWT format**  
* Compact, self-contained ("by value" type of token), portable and web friendly
(can be sent with HTTP header/param etc.)
* Is a STANDARDIZED data format, which mean it works with any language and platform 
supporting it
* Useful in stateless environments, may reduce need for db querying and simplify
load balancing
* Supports symmetric and asymmetric signing, verifiable signatures ensure token
safety (while used correctly)