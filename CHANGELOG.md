# Changelog

## 1.2.2 - 2025-08-13
- Added `ChoiceTheorem's Overhauled Village` villages to `#thief:protected` tag.

## 1.2.1 - 2025-08-04
- Updated zh_cn translation (LogicWheat).

## 1.2.0 - 2025-07-22
- Added hidden advancement when village guard attacks due to low reputation or witnessing a crime.
- Added several KubeJS server events:
  - `ThiefEvents.crimeCommited` - fired when a player would be punished for the crime.
  - `ThiefEvents.giftGiven` - fired when player gives a gift to the villager.
  - `ThiefEvents.reputationLevelChanged` - fired when villager reputation level with entity is changed (includes vanilla reputation changes as well, such as hitting a villager).

## 1.1.1 - 2025-05-31
- Added support for structures that were modified by Lithostitched (thanks 'murphy-slaw').
  - Fixes `#thief:protected` structures not being detected properly with some mods.

## 1.1.0 - 2025-05-29
- Added killing crimes.
  - Controlled by `#thief:killing_protected/<level>` entity-type tag.
  - By default, medium crime is commited when killing animals that are usually found in villages (Horses, Cows, Pigs, Chicken, Cats, etc).
  - Villagers are not included, as they are handled by vanilla already.
- Added ability to show villager reputation tooltip without holding a gift item (client config option). 
- Fixed villager reputation tooltip rendering when gui is hidden. 

## 1.0.1 - 2025-05-24
- Guard witnesses (Iron Golem, etc) will emit angry particles when crime is witnessed.

## 1.0.0 - 2025-05-10
- Release