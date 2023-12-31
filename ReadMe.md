# Projet AMIO

## Vue d'ensemble

L'application AMIO permet à l'utilisateur de détecter si une lumière est allumée alors qu'elle ne
devrait pas l'être.

## Auteurs

Lucie BOUCHER et Nathan CORNELIE

## Organisation des fichiers

Vous pouvez trouver le fichier APK dans le répertoire app/app-debug.apk.

Le code est situé dans le répertoire app/src/main/.

## Fonctionnalités implémentées

Toutes les fonctionnalités requises ont été implémentées :

- Divers services peuvent démarrer lors du démarrage du téléphone.
- L'utilisateur peut configurer le système d'alerte selon ses préférences.
- Les configurations/données sont persistantes.
- L'application détecte correctement lorsque la lumière est allumée.
- Si une lumière est allumée, envoi d'un courriel et d'une notification.

## Utilisation

1. Téléchargez le fichier APK et installez l'application sur votre smartphone Android.

2. Pour que l'application puisse interroger correctement l'IOT LAB, configurez préalablement le VPN
   de l'Université de Lorraine sur votre smartphone Android.

3. Lancez l'application.

ATTENTION : L'application ne fonctionne que si le smartphone est connecté à Internet et à eduram
wifi ou VPN.

4. Configurez l'application selon vos préférences.

5. Appuyez sur le bouton "+" pour seulement faire un appel à l'api et afficher les données .

6. Appuyez sur le bouton avec le symbole infini pour lancer le service de monitoring .
