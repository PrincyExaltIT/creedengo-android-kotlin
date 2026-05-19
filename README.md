<div align="center">
  <picture>
    <source media="(prefers-color-scheme: dark)" width="500" height="100" srcset="docs/resources/creedengo_light.svg">
    <source media="(prefers-color-scheme: light)" width="500" height="100" srcset="docs/resources/creedengo_dark.svg">
    <img alt="Creedengo logo" width="500" height="100" src="docs/resources/creedengo_light.svg">
  </picture>
  <p>
    <strong>A Green Code Initiative project</strong>
  </p>
</div>

---

# Creedengo Android Kotlin

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Contributor Covenant](https://img.shields.io/badge/Contributor%20Covenant-2.1-4baaaa.svg)](https://github.com/green-code-initiative/creedengo-common/blob/main/doc/CODE_OF_CONDUCT.md)

_creedengo_ is a collective project aiming to reduce environmental footprint of software at the code level. The goal of the project is to provide a list of static code analyzers to highlight code structures that may have a negative ecological impact: energy and resources over-consumption, "fatware", shortening terminals' lifespan, etc.

**creedengo-android-kotlin** is the SonarQube plugin that evaluates green code smells in **Android Kotlin** projects. It is the Kotlin counterpart of [creedengo-android (Java)](https://github.com/green-code-initiative/creedengo-android).

The plugin is based on an evolving catalog of [best practices for mobile](https://github.com/cnumr/best-practices-mobile#-android-platform), theorised by Dr. Olivier Le Goaër.

**Warning**: this is still a very early stage project. Any feedback or contribution will be highly appreciated. Please refer to the contribution section.

## 🌿 SonarQube Plugin

This plugin targets:
- Android Kotlin source code
- Android XML configurations

It provides rules to detect energy smells and eco-design anti-patterns specific to Android Kotlin applications.

See also:
- [creedengo-android (Java)](https://github.com/green-code-initiative/creedengo-android) – the Java counterpart
- [creedengo-rules-specifications](https://github.com/green-code-initiative/creedengo-rules-specifications) – all rules specifications

## 🚀 Quickstart

A SonarQube container image with Creedengo Android Kotlin embedded exists!

```bash
docker run -ti --rm \
       -v sq_creedengo_android_kotlin_logs:/opt/sonarqube/logs \
       -v sq_creedengo_android_kotlin_data:/opt/sonarqube/data \
       -p 9000:9000 \
       --name sonarqube-creedengo-android-kotlin \
       ghcr.io/green-code-initiative/sonarqube-creedengo-android-kotlin:latest
```

Wait a little bit during first start initialization, and go to [http://localhost:9000](http://localhost:9000). Default credentials are `admin`/`admin`

## 🛒 Distribution

Ready to use binaries are available [from GitHub](https://github.com/green-code-initiative/creedengo-android-kotlin/releases).

## 🧩 Plugins version compatibility

| Plugins Version | SonarQube version |
|-----------------|-------------------|
| 0.0.+           | TBD               |

## ☕ Plugin compatibility

| Plugins Version | Java version | Kotlin version |
|-----------------|--------------|----------------|
| 0.0.+           | 17           | 2.0+           |

## 🤝 Contribution

You are a technical expert, a designer, a project manager, a CSR expert, an ecodesign expert...

You want to offer the help of your company, help us to organize, communicate on the project?

You have ideas to submit to us?

We are listening to you to make the project progress collectively, and maybe with you!

WE NEED YOU!

Here is the [Starter pack](https://github.com/green-code-initiative/creedengo-common/blob/main/doc/starter-pack.md)

Please also read [`CONTRIBUTING.md`](CONTRIBUTING.md).

## 🤝 Partners


## 📢 Cite this work

If you use Creedengo in an academic work we would be really glad if you cite our seminal paper using the following bibtex entry:

```bibtex
@inproceedings{10.1145/3551349.3559518,
  author = {Le Goaer, Olivier and Hertout, Julien},
  title = {Creedengo: A SonarQube Plugin to Remove Energy Smells from Android Projects},
  year = {2023},
  isbn = {9781450394758},
  publisher = {Association for Computing Machinery},
  address = {New York, NY, USA},
  url = {https://doi.org/10.1145/3551349.3559518},
  doi = {10.1145/3551349.3559518},
  booktitle = {37th IEEE/ACM International Conference on Automated Software Engineering},
  articleno = {157},
  numpages = {4},
  keywords = {android, energy, smells, debt, quality, battery},
  location = {Rochester, MI, USA},
  series = {ASE22}
}
```
