# FromMLWFConfigurationToBPMN
![Java CI with Maven](https://github.com/MireilleBF/FromMLWFConfigurationToBPMN/workflows/Java%20CI%20with%20Maven/badge.svg)



## Overview

The **ml2wf** project aims to help **Data Scientists** creating their workflows in the BPMN standard from a FeatureModel.

You can define your own constraints easily by commenting your workflow using a predefined and customizable syntax.

Once created and modified, the data scientist can merge the workflow into the FeatureModel and reuse it later. Furthermore, this will allow data scientists to use **Meta-learning** and over time to automate the workflows creation.



## Usage

##### NAME

<pre>
ml2wf  Machine Learning problem to Workflow
</pre>
##### SYNOPSIS

<pre>
ml2wf -g -i <ins>file</ins> -o <ins>directory</ins> [-v]
ml2wf -m -i <ins>file</ins> -o <ins>file</ins> [-v]
</pre>
##### DESCRIPTION

<pre> 
-g, --generate    generate a workflow
-m, --merge       import a worklow in a FeatureModel
-i, --input       input file location
-o, --output      output file or directory location
-b, --backup      backup the original FeatureModel file before any modification
-v, --verbose     verbose mode
</pre>



## Example

Lets consider this generic workflow :

![generical_wf](./img/generical_wf.png)

#### Step 1 : Instantiation & Modification

We instantiate our generic workflow using the **generate** (-g) command and we change the tasks names which give us :

![instantiated_wf](./img/instantiated_wf.png)

*Note that we put some constraints on our tasks (in comments).*

#### Step 2 : Merging

We now merge our instantiated workflow in the FeatureModel using the **merge** (-m) command.

Here is the result :

![feature_model](./img/feature_model.png)

#### Step 3  : Reusing your generated tasks for other workflows

Using the FeatureModelIDE, you now can select the wished tasks and it will automatically select the needed ones.

![tasks_selection](./img/tasks_selection.png)