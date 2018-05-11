	var url = 'http://35.198.2.16:3000/temperatura/';
	angular.module('iot', []).controller(
			'sensor',
			function($scope, $http) {
				$scope.GetData = function() {
					$http.get(url).then(function(response) {
						$scope.sensor = response.data;
						$scope.Plot(response.data);
					});

				};

				$scope.SendData = function() {
					var d = new Date();
					var data = {
						time : d.getFullYear() + "-"
								+ ("00" + (d.getMonth() + 1)).slice(-2) + "-"
								+ ("00" + (d.getDate())).slice(-2) + " "
								+ d.toLocaleTimeString(),
						valor : $scope.valor
					};

					$http.post(url, data).then(function(response) {
						$scope.showSuccess = true;
						$scope.GetData();
					}, function(response) {
						$scope.showError = true;
					});
				};

				$scope.Plot = function(data) {
					var x = [], y = [];

					data.forEach(function(item) {
						x.push(item.time);
						y.push(item.valor);
					});

					var charData = [ {
						x : x,
						y : y,
						mode : 'lines+markers',
						line : {
							color : '#80CAF6'
						}
					} ];

					var chart = document.getElementById('chart');

					Plotly.newPlot(chart, charData);
				};

			});