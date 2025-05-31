# Changelog

## UNRELEASED
- Added support for structures that were modified by Lithostitched (thanks 'murphy-slaw').
  - Fixes #protected structures not being detected properly.

## 1.1.0 - 2025-05-29
- Added killing crimes.
  - Controlled by `#thief:protected/<level>` entity-type tag.
  - By default, medium crime is commited when killing animals that are usually found in villages (Horses, Cows, Pigs, Chicken, Cats, etc).
  - Villagers are not included, as they are handled by vanilla already.
- Added ability to show villager reputation tooltip without holding a gift item (client config option). 
- Fixed villager reputation tooltip rendering when gui is hidden. 

## 1.0.1 - 2025-05-24
- Guard witnesses (Iron Golem, etc) will emit angry particles when crime is witnessed.

## 1.0.0 - 1.20.1 - 2025-05-17
- Backport to 1.20.1

## 1.0.0 - 2025-05-10
- Release