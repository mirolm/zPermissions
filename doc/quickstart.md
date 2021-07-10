# Quick Start #

## The Default Group ##

The default group is determined by the `default-group` option in zPermissions's config.yml. Out-of-the-box, the default-group is named `default`. (If you change this option, be sure to `reload` your server or `/permissions reload` zPermissions.)

Regardless of whether or not you change the default group, when zPermissions starts up with a new database or a new flat-file, **it will have no groups defined.**

So one of the first things you'll probably want to do is create the default group.

If you left the default group alone:

    /permissions group default create

Or if you changed it, for example, to `guest`:

    /permissions group guest create

Once this is done, you can start assigning permissions to it.
