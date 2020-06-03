Principes généraux


Voici le FM initial 


Les contraintes sont issues pour l'instant soit de lecture soit de relevé de réunion.

[ ] Faire valider l'ensemble des contraintes

Dans le fichier ci-joint, une version textuelle du FM.


##Configurations

Sur cette base de connaissance assemblée à cette étape sous la forme d'un feature model, nous pouvons commencer à élaguer les possibilités relativement à un problème donné.

1) Par exemple, la présence d'un nombre important d'anomalies interdit d'utiliser la fonction de cout MAE

2) L'exigence de stabilité implique de mettre en place une méthode assurant la stabilité des modèles d'apprentissage contruits

3) Certaines solutions peuvent être contradictoires, donc nous associons dans ce cas nos sources aux contraintes.


Construire le Feature model est ennuyeux et ne capture pas pour l'instant les grands workflows (meta-workflows).

##Des Metaworflows aux workflows instanciés

###Voici un exemple de méta-worflow


###Construire une instance du workflows

###Valider la cohérence du workflows instance et en faire de la connaissance

1) remonter le workflow instance dans le Feature model

2) Vérifier la cohérence du workflow
a. [ ] relativement au méta-workflow (instance => metaworkflow)
b. [ ] relativement à l'ensemble des contraintes (=> erreur de configuration) 


##Faire de la connaissance d'un métaworkflow
a. [ ] remonter les tâches méta
b. [ ] remonter le métaworflow comme une implication de toutes les tâches méta présentes
c. [ ] intégrer dans les contraintes des informations liées aux critères.


##Produire une instance d'un workflow à partir de critères de sélection

a. [ ] Suite à la construction d'une configuration automatiquement sélectionner un métaworkflow et l'ensemble des tâches associées
b. [ ] Générer le workflow instance résultant en respectant les contraintes d'ordre sur les tâches.





