# CHANGELOG

## 0.6.2 Exchange

Minor release with new features and fixes
* added MosaicFactory to better support frequent mosaic instantiation and non-standard network currencies
* added support for exchange transactions
* added support for unsigned version field for entity info on the chain

## 0.6.1

Minor release with new features and fixes
* enhanced transaction builder API

## 0.6.0 Security audit

Breaking changes introduced by the security audit
* DTO changes to match changes in REST API
* e2e test cleanup (+ disabled contract tests as plugin is disabled now)

## 0.5.1 Security audit

Fixing issues caused by changes introduced because of security audit
* remove trailing slash from resource URLs

## 0.5.0 MaxFee calculation and transaction builders

See [milestone](https://github.com/proximax-storage/java-xpx-chain-sdk/milestone/5?closed=1) for fixed issues
* introduced fee calculation strategies
* introduced transaction builders as primary API to create transaction instances
* unified constructors and moved logic to transaction builders
* version field changes (breaks server backward compatibility)
* support for blockchain configuration and upgrade transactions/endpoints
* update of dependency versions

## 0.4.0 Dragon release support

See [milestone](https://github.com/proximax-storage/java-xpx-chain-sdk/milestone/4?closed=1) for fixed issues
* dragon support
* delegated harvesting
* bugfixes

## 0.3.1 Full Cow release support

See [milestone](https://github.com/proximax-storage/java-xpx-chain-sdk/milestone/3?closed=1) for fixed issues
* added support for missing end-points
* transfers to accounts via alias
* proper fee support
* bugfixes, project automation, improved test coverage

## 0.2.0 Initial public release

See [milestone](https://github.com/proximax-storage/java-xpx-chain-sdk/milestone/2?closed=1) for fixed issues
* added support for account properties
* added support for contracts
* published javadoc to https://proximax-storage.github.io/java-xpx-chain-sdk/javadoc/
* bugfixes

## 0.1.0 Initial internal release

Initial public release introducing basic set of features