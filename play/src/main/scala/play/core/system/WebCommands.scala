/*
 * Copyright (C) 2009-2016 Lightbend Inc. <https://www.lightbend.com>
 */
package play.core

import javax.inject.Singleton
/**
 * @see compare-to-play.md
 *
 * Handlers for web commands.
 */
trait WebCommands

/**
 * Default implementation of web commands.
 */
@Singleton
class DefaultWebCommands extends WebCommands
