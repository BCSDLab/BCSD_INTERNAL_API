package com.bcsdlab.internal.bbot.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.bcsdlab.internal.bbot.controller.dto.ThreadCreateRequest;
import com.bcsdlab.internal.bbot.controller.dto.ThreadResponse;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.Conversation;
import com.slack.api.model.Message;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "BBot API")
@SecurityRequirement(name = "JWT")
public interface BBotApi {

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(
                responseCode = "400",
                content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                responseCode = "401",
                content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                responseCode = "403",
                content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                responseCode = "404",
                content = @Content(schema = @Schema(hidden = true))
            ),
        }
    )
    @Operation(summary = "PR 생성")
    @PostMapping("/b-bot/pull-request/thread")
    ResponseEntity<Void> createThread(
        @RequestBody @Valid ThreadCreateRequest threadCreateRequest
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                responseCode = "400",
                content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                responseCode = "401",
                content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                responseCode = "403",
                content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                responseCode = "404",
                content = @Content(schema = @Schema(hidden = true))
            ),
        }
    )
    @Operation(summary = "PR 조회")
    @PostMapping("/b-bot/pull-request/thread")
    ResponseEntity<ThreadResponse> getThread(
        @RequestParam("pullRequestLink") String pullRequestLink
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                responseCode = "400",
                content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                responseCode = "401",
                content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                responseCode = "403",
                content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                responseCode = "404",
                content = @Content(schema = @Schema(hidden = true))
            ),
        }
    )
    @Operation(summary = "채널 ID 조회")
    @GetMapping("/b-bot/channel")
    ResponseEntity<List<Conversation>> getChannel(
    ) throws SlackApiException, IOException;

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                responseCode = "400",
                content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                responseCode = "401",
                content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                responseCode = "403",
                content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                responseCode = "404",
                content = @Content(schema = @Schema(hidden = true))
            ),
        }
    )
    @Operation(summary = "채널 댓글 조회")
    @GetMapping("/b-bot/channel/{id}/message")
    ResponseEntity<List<Message>> getChannelMessage(
        @PathVariable("id") String channelId
    ) throws SlackApiException, IOException;

    // @ApiResponses(
    //     value = {
    //         @ApiResponse(responseCode = "200"),
    //         @ApiResponse(
    //             responseCode = "400",
    //             content = @Content(schema = @Schema(hidden = true))
    //         ),
    //         @ApiResponse(
    //             responseCode = "401",
    //             content = @Content(schema = @Schema(hidden = true))
    //         ),
    //         @ApiResponse(
    //             responseCode = "403",
    //             content = @Content(schema = @Schema(hidden = true))
    //         ),
    //         @ApiResponse(
    //             responseCode = "404",
    //             content = @Content(schema = @Schema(hidden = true))
    //         ),
    //     }
    // )
    // @Operation(summary = "가장 많은 댓글을 올린 5명 조회")
    // @GetMapping("/b-bot/top-poster")
    // ResponseEntity<Void> getTopFivePoster() throws SlackApiException, IOException;
    //
    // @ApiResponses(
    //     value = {
    //         @ApiResponse(responseCode = "200"),
    //         @ApiResponse(
    //             responseCode = "400",
    //             content = @Content(schema = @Schema(hidden = true))
    //         ),
    //         @ApiResponse(
    //             responseCode = "401",
    //             content = @Content(schema = @Schema(hidden = true))
    //         ),
    //         @ApiResponse(
    //             responseCode = "403",
    //             content = @Content(schema = @Schema(hidden = true))
    //         ),
    //         @ApiResponse(
    //             responseCode = "404",
    //             content = @Content(schema = @Schema(hidden = true))
    //         ),
    //     }
    // )
    // @Operation(summary = "내 글에 이모지를 가장 많이 달아준사람 5명조회")
    // @GetMapping("/b-bot/top-reactor")
    // ResponseEntity<Void> getTopFiveReactor() throws SlackApiException, IOException;
    //
    // @ApiResponses(
    //     value = {
    //         @ApiResponse(responseCode = "200"),
    //         @ApiResponse(
    //             responseCode = "400",
    //             content = @Content(schema = @Schema(hidden = true))
    //         ),
    //         @ApiResponse(
    //             responseCode = "401",
    //             content = @Content(schema = @Schema(hidden = true))
    //         ),
    //         @ApiResponse(
    //             responseCode = "403",
    //             content = @Content(schema = @Schema(hidden = true))
    //         ),
    //         @ApiResponse(
    //             responseCode = "404",
    //             content = @Content(schema = @Schema(hidden = true))
    //         ),
    //     }
    // )
    // @Operation(summary = "내가 가장 많이 사용한 이모지 5개")
    // @GetMapping("/b-bot/most-used-emojis")
    // ResponseEntity<Void> getTopFiveUsedEmojis() throws SlackApiException, IOException;
    //
    // @ApiResponses(
    //     value = {
    //         @ApiResponse(responseCode = "200"),
    //         @ApiResponse(
    //             responseCode = "400",
    //             content = @Content(schema = @Schema(hidden = true))
    //         ),
    //         @ApiResponse(
    //             responseCode = "401",
    //             content = @Content(schema = @Schema(hidden = true))
    //         ),
    //         @ApiResponse(
    //             responseCode = "403",
    //             content = @Content(schema = @Schema(hidden = true))
    //         ),
    //         @ApiResponse(
    //             responseCode = "404",
    //             content = @Content(schema = @Schema(hidden = true))
    //         ),
    //     }
    // )
    // @Operation(summary = "가장 시끄러웠던 팀 또는 트랙")
    // @GetMapping("/b-bot/most-active-track-or-team")
    // ResponseEntity<Void> getMostActiveTrackOrTeam() throws SlackApiException, IOException;

}
