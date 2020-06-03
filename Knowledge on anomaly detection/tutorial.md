# Principes généraux


Voici le FM initial 
Les contraintes sont issues pour l'instant soit de lecture soit de relevé de réunion.

![Feature Model](/Knowledge%20on%20anomaly%20detection/img/AD.png)


[ ] Faire valider l'ensemble des contraintes

Dans le fichier ci-joint, une version textuelle du FM.
![description](/Knowledge%20on%20anomaly%20detection/FMs/ADDepencies.txt)



## Configurations

Sur cette base de connaissance, assemblée à cette étape sous la forme d'un feature model, nous pouvons commencer à élaguer les possibilités relativement à un problème donné.

1) Par exemple, la présence d'un nombre important d'anomalies interdit d'utiliser la fonction de cout MAE

<img src="/Knowledge%20on%20anomaly%20detection/img/configuration_MAE.png" alt="configuration_MAE" width="500"/>

2) L'exigence de stabilité implique de mettre en place une méthode assurant la stabilité des modèles d'apprentissage contruits

<img src="/Knowledge%20on%20anomaly%20detection/img/configuration_stability.png" alt="configuration_stability" width="500"/>

3) Certaines solutions peuvent être contradictoires, donc nous associons dans ce cas nos sources aux contraintes.

<img src="/Knowledge%20on%20anomaly%20detection/img/configuration_and_reference.png" alt="configuration_and_reference" width="500"/>

**Construire le Feature model est ennuyeux et ne capture pas pour l'instant les grands workflows (meta-workflows).**

## Des Metaworkflows aux workflows instanciés

### Voici un exemple de méta-worflow

<img src="/Knowledge%20on%20anomaly%20detection/img/MetaModel_AD.png" alt="drawing" width="500"/>

### Construire une instance du workflows
Voici un exemple d'une instance de workflow :

<img src="/Knowledge%20on%20anomaly%20detection/img/instance_model_AD.png" alt="drawing" width="500"/>

Voici une instance de workflow généré : 
[ ] TO DO ligne de commande + image

### Valider la cohérence du workflows instance et en faire de la connaissance

1) remonter le workflow instance dans le Feature model

2) Vérifier la cohérence du workflow
a. [ ] relativement au méta-workflow (instance => metaworkflow)
b. [ ] relativement à l'ensemble des contraintes (=> erreur de configuration) 


## Faire de la connaissance d'un métaworkflow
a. [ ] remonter les tâches méta
b. [ ] remonter le métaworflow comme une implication de toutes les tâches méta présentes
c. [ ] intégrer dans les contraintes des informations liées aux critères.


## Produire une instance d'un workflow à partir de critères de sélection

a. [ ] Suite à la construction d'une configuration automatiquement sélectionner un métaworkflow et l'ensemble des tâches associées
b. [ ] Générer le workflow instance résultant en respectant les contraintes d'ordre sur les tâches.





