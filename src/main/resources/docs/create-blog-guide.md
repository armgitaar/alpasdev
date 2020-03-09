- [What you'll build](#build)
- [What you'll learn](#learn)
- [What you'll need](#need)
- [Step 1: Start your project](#initial-setup)
- [Step 2: Setup your database and blogs table](#create-database)
- [Step 3: Setup the routes](#setup-routes)
- [Step 4: Create service provider](#create-service-provider)
- [Step 5: Create a markdown convertor](#markdown-convertor)
- [Step 6: Create blog controller](#create-blogcontroller)
- [Step 7: Create a validation rule](#validation-rule)
- [Step 8: Create the front-end](#front-end)
- [Step 9: Run the app and blog away!](#run-app)


<a name="build"></a>
### [What you'll build](#build)
In this guide, you'll learn how to create your very own, fully functional blog. 

With just a little added TLC and a dash of personal flair, you will be in the position to deploy out your very own blog
with it's own lightweight Content Management System (CMS). All built by you! 📰

<a name="learn"></a>
### [What you'll learn](#learn)
>/tip/ 
> <span>You'll learn a great deal creating your own blog website
>
><div class="sublist">
>
>- How to create a service provider
>- How to create a custom rule
>- How to convert markdown to HTML
>- How to validate user input entries and persist entry data if failed
>- How to apply pagination to list results and as previous/next blog post navigation
>- How to apply Tailwind CSS for front-end styling
>
></div>

You're on your way to becoming an Alpas Master! 🎓

<a name="need"></a>
### [What you'll need](#need)

This guide assumes that you have already worked through the [Quick Start Guide](/docs/quick-start-guide-todo-list) where you created a To-Do task list. We will leave out the 
basic setting up of a new project steps in this guide and we will be a little more straightforward overall as we expect that you now have mastered 
some Alpas basics. 💪🏽

Other than the above, there is nothing else that you will need other than some time. **This guide will take you less than an hour to implement** if you go for 
straight copy-and-paste with some review of code to gain understanding of the steps you're taking. 


<div class="border-t mt-8"></div>

### Let's get the blogging started! 📝

<a name="initial-setup"></a>
### [Step 1: Start your project](#initial-setup)

Go to the [Alpas Starter project](https://github.com/alpas/starter) on GitHub and setup your new blog using the stater template. 
Perform the initial steps required to successfully run your starter template. 


<a name="create-database"></a>
### [Step 2: Setup your database and blogs table](#create-database)

<div class="ordered-list mb-4">

1. Setup a new local database and connect to it from within your project. 
2. Create a new blog entity using `./alpas make:entity blog`<span class="clipboard" data-clipboard-text='./alpas make:entity blog'></span> command
3. Update the **Blog.kt** entity file to include id, content, created_at, and updated_at fields
4. Create a blogs table using `./alpas make:migration create_blogs_table --create=blogs`<span class="clipboard" data-clipboard-text='./alpas make:migration create_blogs_table --create=blogs'></span> command
5. Migrate your blogs table to your database using `./alpas db:migrate`<span class="clipboard" data-clipboard-text='./alpas db:migrate'></span> command

</div>

Perfecto! We have completed the database portion. Now, let's get into creating routes. 


>/tip/ 
> Check your progress against ENTER LINKS TO PAGES ON GITHUB

>/info/
><strong>Related Documentation</strong> 
><div class="sublist">
>
>- [Entity Relationship][Entity Relationship]
>- [Migrations][Migrations]
>
></div>

<a name="setup-routes"></a>
### [Step 3: Setup the routes](#setup-routes)

Routes. It's where routing happens! 😂

Add the following routes to the **routes.kt** file - 

<span class="line-numbers" data-start="16" data-file="src/main/kotlin/routes.kt">

```kotlin
private fun RouteGroup.webRoutesGroup() {
    get("/", BlogController::index).name("welcome")
    get("/<page>", BlogController::pages).name("pages")
    get("blog/<id>/<page>", BlogController::show).name("blog.show")
    get("blog/new", BlogController::new).name("blog.new")
    post("blog/submit", BlogController::submit).name("blog.submit")
    get("blog/edit/<id>", BlogController::edit).name("blog.edit")
    patch("blog/edit/<id>", BlogController::update).name("blog.update")
    delete("blog/<id>", BlogController::delete).name("blog.delete")
}
```
</span>

Notice, we removed the WelcomeController route as we will only use BlogController. You can update the WelcomeController 
import to be BlogController. We will create the BlogController in a couple steps. 

Browsing through the different routes, you can easily gather what the main user interactions will be on the front-end: 

<div class="sublist">

- Main index / welcome page
- Paging for when your blog has multiple posts - in this exercise we will display 9 posts per page
- Showing the blog post detail page
- Submitting a new blog post
- Enter an Edit mode for blog posts where you update the post's content
- Remove a blog post

</div>

Next, let's make a service provider. 


>/tip/ 
> Check your progress against ENTER LINKS TO PAGES ON GITHUB

>/info/
><strong>Related Documentation</strong> 
><div class="sublist">
>
>- [Routing][Routing]
>
></div>

<a name="create-service-provider"></a>
### [Step 4: Create service provider](#create-service-provider)

Service providers are classes that allow you to register and boot your services in a more controlled way in one central place. 
Our service provider will be simple and assist with making blogs. 

<div class="ordered-list mb-4">

1. Use the `./alpas make:provider BlogServiceProvider`<span class="clipboard" data-clipboard-text='./alpas make:provider BlogServiceProvider'></span> command to 
create the service provider
2. In the **BlogServiceProvider** file, add `app.bind(Blogs(app.make()))`<span class="clipboard" data-clipboard-text='app.bind(Blogs(app.make()))'></span> to the `override fun boot` function
3. Navigate to the **HttpKernal** file and add `BlogServiceProvider::class`<span class="clipboard" data-clipboard-text='BlogServiceProvider::class'></span> to the list of providers

</div>

Let's next code the logic to convert markdown to HTML as well as extract some blog post meta data. 


>/tip/ 
> Check your progress against ENTER LINKS TO PAGES ON GITHUB

>/info/
><strong>Related Documentation</strong> 
><div class="sublist">
>
>- [Service Provider][Service Provider]
>
></div>


<a name="markdown-convertor"></a>
### [Step 5: Create a markdown convertor](#markdown-convertor)

Our blog will allow a contributer to format content using markdown. Once they save their changes, our code will convert the 
markdown content to HTML. We will also write some logic to read through post headers to extract meta data.  

<div class="ordered-list pb-4">

1. Create a new directory under the Kotlin directory named **actions**
2. Under the actions directory, create a new file named **Blogs.kt**
3. Open **Blogs.kt** and add the code from [ENTER GITHUB URL]

</div>

You will notice that we import the Flexmark library. If you didn't know it already, Kotlin is compatible with Java libraries - 
so we can natively pull in and use the best Java based modules out there! 

>/tip/ 
> Be sure to look through the code and the comments to gain a better understanding of the operations being performed. 

>/info/
><strong>Related Documentation</strong> 
><div class="sublist">
>
>- [Flexmark][Flexmark]
>
></div>


<a name="create-blogcontroller"></a>
### [Step 6: Create blog controller](#create-blogcontroller)

We are super close - just a few more steps! 

Let's now create the largest piece of the puzzle, the blog controller. 

<div class="ordered-list">

1. Create a new controller using `./alpas make:controller BlogController`<span class="clipboard" data-clipboard-text='./alpas make:controller BlogController'></span> 
2. In the newly created **BlogController.kt** file, copy and paste in the code from [ADD GITHUBLINK]

</div>

You may notice under the submit function we are referring to a `BlogData` validation rule. This is a custom rule that we will
setup in the next step. 

>/tip/
> A lot of the brains are in this file. Read through the code and pay extra attention to the comments. Make sure you have 
> a good understanding of what's going on in this file. 

>/info/
><strong>Related Documentation</strong> 
><div class="sublist">
>
>- [Controllers][Controllers]
>- [Ktorm][Ktorm]
>
></div>


<a name="validation-rule"></a>
### [Step 7: Create a validation rule](#validation-rule)

There is no out-of-the-box rule to support validating the blog post content. Luckily, it's easy to create a new custom rule 
that can be called to validate a form entry. 

In this case, we want to validate all of the meta data fields are accounted for and
that there is at least some content in the body of the blog post. 

<div class="ordered-list">

1. Create a new validation rule using the `./alpas make:validation-rule BlogData`<span class="clipboard" data-clipboard-text='./alpas make:validation-rule BlogData'></span> command
2. Open up the **BlogData.kt** file and add in the below code
3. After updating the file, don't forget to import the new rule into the **BlogController.kt** file

</div>

<span class="line-numbers" data-start="1" data-file="src/main/kotlin/rules/BlogData.kt">

```kotlin
package com.blog.blogify.rules

import dev.alpas.validation.ErrorMessage
import dev.alpas.validation.Rule
import dev.alpas.validation.ValidationGuard

class BlogData(private val markData: String, private val title: String, private val author: String, private val date: String, private val image: String, private val tags: String, private val message: ErrorMessage = null) : Rule() {
    override fun check(attribute: String, value: Any?): Boolean {

        val isValid = markData.isNotEmpty() && title.isNotEmpty() && author.isNotEmpty() && date.isNotEmpty() && image.isNotEmpty() && tags.isNotEmpty()

        if (!isValid) {

            error = message?.let { it(attribute, value) } ?: "Submission failed - be sure to fill in all metadata and blog content."
        }
        return isValid
    }
}

fun ValidationGuard.blogData(markData: String, title: String, author: String, date: String, image: String, tags: String, message: ErrorMessage = null): Rule {
    return rule(BlogData(markData, title, author, date, image, tags, message))
}
```

</span>

The rule simply checks to make sure all of the required content is present. As an added exercise, try to see if you can customize the validation to 
tell the front-end exactly which piece(s) failed validation. 

Now all we need to do is create the front-end!

>/info/
><strong>Related Documentation</strong> 
><div class="sublist">
>
>- [Validation Rules][Validation Rules]
>
></div>


<a name="front-end"></a>
### [Step 8: Create the front-end](#front-end)

For the front-end of the website, we will be using [Tailwind CSS](https://tailwindcss.com). The Alpas start project already comes pre-installed with 
Tailwind. All we need to do is customize it a bit and then apply some classes to create a slick looking interface! 

For the majority of the step, feel free to get creative and let your design-side shine! 👩‍🎨


<div class="ordered-list">

1. Open the **app.less** file and let's add some custom CSS to target HTML tags that were applied during the markdown to HTML conversion
2. Add the CSS from [link to github page]; alternatively, you can work with the tailwind.config.js file to customize HTML tag styles
3. Under the **templates** directory, create the following files
4. **_footer.peb** and add contents from [link to  github page]
5. **_header.peb** and add contents from [link to  github page]
6. **_nav.peb** and add contents from [link to  github page]
7. **blog.peb** and add contents from [link to  github page]
8. **editblog.peb** and add contents from [link to  github page]
9. **marked.peb** and add contents from [link to  github page]
10. **new.peb** and add contents from [link to  github page]
11. Modify **welcome.peb** by replacing current contents with contents from [link to  github page]

</div>

>/tip/ 
> If you change the css.less file or the tailwind.config.js file, you will need to recompile your project. You can do that by 
> installing yarn for your project and then run 'yarn development' or 'yarn watch' commands

>/info/
><strong>Related Documentation</strong> 
><div class="sublist">
>
>- [Tailwind CSS][Tailwind CSS]
>- [Pebble Templates][Pebble Templates]
>-[Mixing Assets][Mixing Assets]
>
></div>


<a name="run-app"></a>
### [Step 9: Run the app and blog away!](#run-app)

Now for the fun part, run your new blog website on you local environment and blog away!

**If you want to take your blog all the way, do the following:** 

<div class="ordered-list">

1. Add [authentication](https://alpas.dev/docs/authentication) and put the add/edit/remove blog post controls behind an authentication gate
2. [Provision a server and deploy](/docs/deployment) your blog so that it is live on the world wide web for everyone to read
</div>

[Entity Relationship]: https://alpas.dev/docs/entity-relationship#main
[Migrations]: https://alpas.dev/docs/migrations/
[Routing]: https://alpas.dev/docs/routing
[Service Provider]: https://alpas.dev/docs/service-providers#main
[Flexmark]: https://github.com/vsch/flexmark-java
[Controllers]: https://alpas.dev/docs/controllers#main
[Ktorm]: https://ktorm.liuwj.me/
[Validation Rules]: https://alpas.dev/docs/validation#validation-rules
[Tailwind CSS]: https://tailwindcss.com/
[Pebble Templates]: https://alpas.dev/docs/pebble-templates
[Mixing Assets]: https://alpas.dev/docs/mixing-assets