/*
 * Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com>
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
