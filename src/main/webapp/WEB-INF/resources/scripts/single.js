(function($){  
	var methods = {
		// Initialise plugin.
		init: function() {
            methods.loadActivity($("#activityId").val());
		},
        
        loadActivity: function(id) {
            $.ajax({
                url:rootUrl + '/match/details/' + id,
                type:'GET',
                success:function(data, status, jqXHR){
                    $('#match').append('<div class="activityData">' + methods.showActivityHeader(data) + '</div>');
                    $('#match-detail').append('<div class="activityData">' + methods.showActivity(data) + '</div>');
                },
                error:function(jqXHR, status, error){
                    $('#match').append('<div class="activityData">ERROR</div>');
                },
                complete:function(jqXHR, status){
                    $('.loading-spinner').hide();
                }
            });
        },
        
        showActivityHeader: function(data) {
            var matchOutput = '';
            matchOutput += '<div class="panel panel-default match">';
            matchOutput += '<div class="panel-heading"><div class="tableDiv"><div class="activityCell"><img class="activityIcon" src="' + data.activityIcon + '" title="' + data.activityName + '" /></div>';
            matchOutput += '<div class="dateCell">' + convertDateString(data.period) + '</div>';
            matchOutput += '<div class="fillCell">' + data.duration + '</div>';
            matchOutput += '</div></div>';
            matchOutput += '<div class="panel-body" id="match-detail">';
            matchOutput += '</div>';
            matchOutput += '</div>';
            return matchOutput;
		},
        
        showActivity: function(data) {
            if (data.teamStats && data.teamStats.length) {
                return methods.showTeams(data.teamStats);
            } else {
                return methods.showPlayers(data.playerStats);
            }
        },
        
        showTeams: function(teamStats) {
            output = '';
            for (var t in teamStats) {
                teamStat = teamStats[t];
                output += '<div class="panel ' + panelColour(teamStat.name) + '">';
                output += '<div class="panel-heading">';
                output += teamStat.name + ' ' + new Intl.NumberFormat().format(teamStat.score) + ' ' + teamStat.result;
                output += '</div>';
                output += methods.showPlayers(teamStat.playerStats);
                output += '</div>';
            }
            return output;
        },
        
        showPlayers: function(playerStats) {
            var hasScores = playerStats[0].score != "0"
            output = '<table class="table table-striped players">';
            output += '<thead>';
            output += '<tr>';
            output += '<th></th>';
            output += '<th>K</th>';
            output += '<th>A</th>';
            output += '<th>D</th>';
            output += '<th>K/D</th>';
            if (hasScores) {
                output += '<th>S</th>';
            }
            output += '</tr>';
            output += '</thead>';
            output += '<tbody>';
            for (var p in playerStats) {
                playerStat = playerStats[p];
                output += '<tr class="player">';
                output += '<td rowspan="3" class="iconCell"><img class="activityIcon" src="' + playerStat.playerIcon + '" title="' + playerStat.name + '" /></td>';
                output += '<td>' + playerStat.k + '</td>';
                output += '<td>' + playerStat.a + '</td>';
                output += '<td>' + playerStat.d + '</td>';
                output += '<td>' + padDecimal(playerStat.kd, 2) + '</td>';
                if (hasScores) {
                    output += '<td>' + new Intl.NumberFormat().format(playerStat.score) + '</td>';
                }
                output += '</tr>';
                output += '<tr class="invisRow"></tr>';
                output += '<tr class="player">';
                output += '<td colspan="3">' + playerStat.name + '</td>';
                output += '<td colspan="2" style="font-size: smaller; text-align: right">' + playerStat.classType + ' ' + playerStat.level + '</td>';
                output += '</tr>';
            }
            output += '</tbody></table>'
            return output;
        }

	};
	
	$(document).ready(function(){
		methods.init();
	});
	
})(jQuery);
