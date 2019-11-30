- [Introduction](#introduction)
- [Returning Response Payload](#response-payload)
- [Rendering Templates](#rendering-templates)
- [Aborting Calls](#abort)
- [Headers](#headers)
    - [Attaching Headers](#attaching-headers)
    - [Content Type](#content-type)
- [Cookies](#cookies)
    - [Attaching Cookies](#attaching-cookies)
    - [Forgetting Cookies](#forgetting-cookies)
- [Redirects](#redirects)

<a name="introduction"></a>
### [Introduction](#introduction)

Just like [HTTP request](/docs/http-request), `HttpCall` object is what you'd use for sending responses back to 
a client.

<br/>

<a name="reesponse-payload"></a>
### [Returning Response Payload](#response-payload)

You can send response back to the client with a "payload" by using on of `reply()` or `replyAsJson()`. There are
some overloaded variants of these methods as well.

<div class="sublist">

- `reply(payload: T? = null, statusCode: Int = 200)`

Returns a payload back to the client as a UTF-8 encoded HTML format. The payload is first converted to a string 
by calling`toString()` method on it. You can pass the status code if you want it to be something other than the 
default 200.

- `replyAsJson(payload: T? = null, statusCode: Int = 200)`

Returns the given payload to the client in a JSON format.

- `replyAsJson(obj: JsonSerializable, statusCode: Int = 200)`

A convenient method for returning an object that implements `dev.alpas.JsonSerializable` interface. The returned format
is JSON, of course!

- `acknowledge(statusCode: Int = 204)`

Sometimes you may want to just acknowledge a request call and not necessarily have any payload to return. In such
a situation you could use this convenient method. Since, the acknowledgement without a payload is mostly applicable in
a JSON request, the content type is set to JSON when using this method.

- `status(code: Int)`

Use this method if you want to set/change the status code of your response *after* calling one of the above methods.

</div>

<br/>

<a name="rendering-templates"></a>
### [Rendering Templates](#rendering-templates)

Instead of returning a string or a `JsonSerializable`, you can render a template instead using one of render functions.

<div class="sublist">

- `render(templateName: String, args: Map<String, Any?>? = null, statusCode: Int = HttpStatus.OK_200)`

Render a template of the given name with optionally some arguments for the template. The location of the template, by 
default, is under `/src/main/resources/templates`. You could override the location by 
[extending](/docs/configuration#core-configs) the `dev.alpas.view.ViewConfig` class.

</div>

<br/>

<a name="abort"></a>
### [Aborting Calls](#abort)

Instead of returning a valid response, you may want to abort a call and return an error response back to the client.
You can do so by using `abort()` or `abortUnless()` method.

<div class="sublist">

- `abort(statusCode: Int, message: String? = null, headers: Map<String, String> = emptyMap()): Nothing`

Aborts the call and sends back an exception appropriate for the given status code. **404** code is converted 
to `NotFoundHttpException`, **405** to `MethodNotAllowedException`, **500** to `InternalServerException`, and other 
exceptions to `HttpException`.

- `abortUnless(condition: Boolean, statusCode: Int, message: String? = null, headers: Map<String, String> = emptyMap())`

Aborts the call if the given condition is `false`.

- `abortIf(condition: Boolean, statusCode: Int, message: String? = null, headers: Map<String, String> = emptyMap())`

Aborts the call if the given condition is `true`.

> /tip/ <span>You can simply throw a subclass of `dev.alpas.exceptions.HttpException` if you  want to have more
> control over the exception being thrown when aborting a call. </span>

</div>

<br/>

<a name="headers"></a>
### [Headers](#headers)

<a name="attaching-headers"></a>
#### [Attaching Headers](#attaching-headers)

Use `addHeader(key: String, value: String)` to attach a header or `addHeaders(headers: Map<String, String>)` 
to attach multiple headers to an outgoing response. You can call these multiple times to attach additional headers.

<span class="line-numbers" data-start="7">

```kotlin
fun index(call: HttpCall) {
    call.apply {
        addHeader("Content-Type", "application/json")
        addHeader("X-H1", "Some Value")
        addHeader("X-H2", "Some Value")
    }.reply("Headers Galore!")
 }
```

</span>

<a name="content-type"></a>
#### [Content Type](#content-type)

<div class="sublist">

- `asHtml()` - Set the content type of the response to **"text/html; charset=UTF-8"**
- `asJson()` - Set the content type of the response to **"application/json; charset=UTF-8"**
- `contentType(type: String)` - Set the content type of the response to the given type.

<span class="line-numbers" data-start="7">

```kotlin
fun index(call: HttpCall) {
    val content = mapOf("say" to "Hello, Json!").toJson()
    call.reply(content).asJson()   
    // alternatively
    call.replyAsJson(content)
 }
```

</span>

</div>

<a name="cookies"></a>
### [Cookies](#cookies)

Cookie is a small piece of information that gets sent with a response back to the client. The client would then
return unexpired cookies [back to the server in the subsequent requests](#/docs/http-request#retrieving-cookies).

> /info/ <span> Alpas encrypts and signs almost all of your outgoing cookies and decrypts them when it receives a 
> request. If the cookies are changed by the client, they will be invalidated and removed automatically as well.

> /tip/ <span>You can return the names of the cookies that you **don't** want to be encrypted by [extending 
> `SessionConfig`](/docs/configuration#core-configs) class and then overriding `encryptExcept` value.</span>

<a name="attaching-cookies"></a>
#### [Attaching Cookies](#attaching-cookies)

Send a cookie back to the client by calling one of `addCookie()` methods.

<div class="sublist">

- `fun addCookie(name: String, value: String?, lifetime: Duration, path: String?, domain: String?, secure: Boolean, httpOnly: Boolean)`

    <br/>
    
    - **name** - The name of the cookie. Must not be empty.
    - **value** - The payload of the cookie.
    - **lifetime** - Maximum age for this cookie. Default is -1 second. A negative duration means this cookie will be
    deleted when the browser exits. A zero duration means the cookie will be deleted right away.
    - **path** - The path for the cookie to which the client should return the cookie.  Default is `null`.
    - **domain** - The domain within which the cookie should be presented. Default is `null`.
    - **secure** - Whether the browser should return this cookie only using a secure protocol, such as HTTPS or SSL, 
    or not. Default is `false`.
    - **httpOnly** - Whether to mark this cookie as *HttpOnly* or not. This directs a browser to not expose this cookie
    to client-side scripting code. Default is `true`.

</div>

<a name="forgetting-cookies"></a>
#### [Forgetting Cookies](#forgetting-cookies)

You can forget/ clear a cookie by calling `forgetCookie()` method and passing the name of the cookie that you want to
clear. Optionally, you can also pass the cookie's `path` and/or the `domain`.

<a name="redirects"></a>
### [Redirects](#redirects):

The `HttpCall#redirect()` method returns an implementation of `dev.alpas.http.Redirectable`, which has everything
you'd need to redirect a call to somewhere else — either internal or external.

<div class="sublist">

- `fun to(to: String, status: Int = 302, headers: Map<String, String> = emptyMap())`

Redirects a call to the given **to** url.

- `fun back(status: Int = 302, headers: Map<String, String> = emptyMap(), default: String = "/")`

Redirects a call to the previous location. The previous location is determined by first looking at the referral
header in the request. If it is null then it looks the value of a previous url from the session. Alpas automatically
saves this location in the current user session for every request. If both of these values don't exist, then it will
redirect to the given *default* location.

This method is pretty handy for sending a user back to a form if the form input is invalid. In fact, this is what 
Alpas exactly does for validation errors.

- `fun intended(default: String = "/", status: Int = 302, headers: Map<String, String> = emptyMap())`

Redirects a call to the location that the user initially intended to go to. Let's say a user is trying to access
a page that requires the user to be authenticated. In this case, we'd normally take the user to a login page so that
she could be authenticated. After she successfully login, we would want to take her to the page she originally 
intended. In this case you'd want to use `intended()` method.

- `fun toRouteNamed(name: String, params: Map<String, Any> = emptyMap(), status: Int = 302, headers: Map<String, String> = emptyMap())`

Redirects a call to a route that matches the given **name**. **params** is a map of parameters for that route.

<span class="line-numbers" data-start="7">

```kotlin
fun index(call: HttpCall) {
    call.redirect().toRouteNamed("docs.showPage", mapOf("page" to "http-response"))
 }
```

</span>

- `fun home(status: Int = HttpStatus.MOVED_TEMPORARILY_302, headers: Map<String, String> = emptyMap())`

A convenient method that redirects a call to a route named **home**, if exists. If not, it redirects to **/**.

- `fun toExternal(url: String, status: Int = 302, headers: Map<String, String> = emptyMap())`

Redirects a call to the given external url.

</div>