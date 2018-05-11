

<body ng-app="iot" ng-controller="sensor" ng-init="GetData()">
	<div class="container text-left">    
 
	<div class="row">
    	<div class="col-sm-6">
    	<div>
    	<h3>Meus Sensores</h3> 
    	<div class="table-responsive"> 
    	<table class="table table-striped">
					<thead class="thead-dark">
						<tr>
							<th>Id</th>
							<th>Data/Hora Leitura</th>
							<th>Temperatura</th>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="s in sensor">
							<td>{{s._id}}</td>
							<td>{{s.time}}</td>
							<td>{{s.valor}}</td>
						</tr>
					</tbody>
				</table>         
		  </div>
		  </div>
		  </div>
		  			<div class="col-sm-6">
		  			<h3> &Uacute;ltimas leituras:</h3> 
				<div id="chart" class="responsive-plot"></div>
			</div>
			</div>


			
</body>