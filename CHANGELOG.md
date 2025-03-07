# PNC Ocean Addons - Changelog

This is an overview of significant new features and fixes by release.  See https://github.com/TeamPneumatic/pnc-ocean-addons/commits/main for a detailed list of all changes.

Changes are in reverse chronological order; newest changes at the top.

# Minecraft 1.21.1

## [1.0.3]

### Added
* Added support for alternative armors for crush protection, using the `` item tag.
  * Config setting: `require_full_tagged_set` if true requires _every_ piece to be in the tag; if false, one piece is enough

## [1.0.2]

### Fixed
* Fix creaking sounds continually playing in the death screen menu

## [1.0.1]

### Added
* Added tooltips to depth upgrade items indicating max safe depth
* Added "head_must_be_in_fluid" config setting, default true
  * When true, player's head must be in a fluid to trigger depth damage
  * When false, only depth below sea level is considered (i.e. air pockets deep down are not safe!)

### Fixed
* Fixed depth upgrade stat panel not opening when it should

## [1.0.0]

* Initial release
